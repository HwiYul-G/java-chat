# Artificial Intelligence, AI
##### ✏️ 목표
채팅 서비스에 제공할 비속어 탐지 모델 만들기</br>
##### 📑 목차
1. [사용 데이터](#사용-데이터)  
   1. [데이터 형식](#데이터-형식)
   2. [데이터 예시](#데이터-예시)
2. [**코드 전 필수 지식**](#코드-전-필수-지식)
3. [학습 및 평가 코드](#학습-및-평가-코드)
   1. [데이터 전처리](#데이터-전처리)
      1. [시작과 끝 토큰 추가](#시작과-끝-토큰-추가)
      2. [토큰화](#토큰화)
      3. [정수 인코딩과 제로 패딩](#정수-인코딩과-제로-패딩)
      4. [어텐션 마스크 생성](#어텐션-마스크-생성)
   2. [모델 설정 및 학습](#모델-설정-및-학습)
      1. [Bert 모델 불러오기](#bert-모델-불러오기)
      2. [학습 설정 셋팅](#학습-설정-셋팅)
      3. [학습 과정](#학습-과정)
   3. [평가 및 결과](#평가-및-결과)
      1. [평가 및 결과](#평가-및-결과)
      2. [새로운 문장 테스트](#새로운-문장-테스트)

## 사용 데이터
🔗 **[사용 데이터 링크](https://github.com/2runo/Curse-detection-data)**  
5828개의 댓글 데이터로, 각 댓글의 내용과 비속어 포함 여부가 `|` 구분자로 나뉘어 있습니다.

### 데이터 형식
- **총 수량**: 5,828개
- **구분 기준**: 
  - `0`: 비속어 없음
  - `1`: 비속어 있음
- **비속어 판단 기준**
  - 단순히 비속어 포함 유무가 아니라, 댓글의 전체 **맥락**을 통해 비속어로 판단한다.

### 데이터 예시
```text
집에 롱패딩만 세 개다. 10년 더 입어야지 | 0
애새끼가 초딩도 아니고 ㅋㅋㅋㅋ	| 1
```

## 코드 전 필수 지식
#### 샘플 문장
```text
문장1: 파리는 아름다운 도시이다.
문장2: 나는 파리를 사랑해요.
```
### 입력 데이터 표현
- 토큰 임베딩, 세그먼트 임메딩, 포지션(위치) 임베딩

#### 토큰 임베딩
- `[CLS]`를 시작에 붙이고 `[SEP]`를 끝에 붙인다.
  - `[CLS]`는 분류 작업을 위해 사용한다.
  - `[SEP]`는 문장의 끝에 등장한다.
- 문장1: [[cls], 파리는, 아름다운, 도시이다., [sep], 나는, 파리를, 사랑해요.]

#### 세그먼트 임베딩
문장1에 속하는지 문장2에 속하는지 표기한다.
```text
[CLS] : 문장1
파리는: 문장1
아름다운: 문장1
도시이다.: 문장1
[SEP]: 문장1
나는: 문장2
파리를: 문장2
사랑해요.: 문장2
```

#### 포지션 임베딩
- 트렌스포머는 recurrence 메커니즘을 사용하지 않고 모든 단어를 병렬로 처리하기 때문에, 단어 순서와 관련된 정보가 필요하다.
- 그래서 positional encoding이 필요하다.

### WordPiece Tokenizer
- 워드피스 토크나이저는 서브워드 토크나이저 스키마를 따른다. 
- `Let us start pretraining the model`를 워드 피스를 사용해
- 토크나이즈하면 `[let, us, start, pre, ###train, ###ing, the, model]`이 된다.
- vocabulary words에서 다룰 수 있을 정도로 word를 재귀적으로 쪼갠다.

## 학습 및 평가 코드
### 데이터 전처리
#### 시작과 끝 토큰 추가
```python
sentences = ['[CLS]' + str(s) + '[SEP]' for s in train['content]]
```
#### 토큰화
```python
# 단어를 토큰화한다. `##` 같은 것이 쪼개서 붙는다.
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased', do_lower_case=False)
tokenized_texts = [tokenizer.tokenize(s) for s in sentences]
```
#### 예시
```text
[CLS] 석유 화학은 GS, S-오일, 현대오일뱅크지 [SEP]
['[CLS]', '석', '##유', '화', '##학', '##은', 'GS', ',', 'S', '-', '오', '##일', ',', '현대', '##오', '##일', '##뱅', '##크', '##지', '[SEP]']
```
#### 정수 인코딩과 제로 패딩
```python
MAX_LEN = 128

input_ids = [tokenizer.convert_tokens_to_ids(x) for x in tokenized_texts]
input_ids = pad_sequences(input_ids, maxlen=MAX_LEN, dtype="long", truncating="post", padding="post")
```
#### 어텐션 마스크 생성
```python
attention_masks = []

# input_ids에는 문장을 128개의 시퀀스로 나눈 데이터가 들어있음
for seq in input_ids: 
  seq_mask = [ float(i > 0) for i in seq ]
  attention_masks.append(seq_mask)

# attention mask에는 데이터의 유무(1 or 0)만 들어가 있음
```

### 모델 설정 및 학습
#### Bert 모델 불러오기
```python
model = BertForSequenceClassification.from_pretrained("bert-base-multilingual-cased", num_labels=2)
model.cuda()
```
#### 학습 설정 셋팅
```python
# 옵티마이저
optimizer = AdamW(
  model.parameters(),
  lr = 2e-5, # 학습률(learning rate)
  eps = 1e-8
)

# 에폭수
epochs = 4

# 총 훈련 스텝: 배치반복 횟수 * 에폭
total_steps = len(train_dataloader) * epochs

# 스케줄러 생성
scheduler = get_linear_schedule_with_warmup(
  optimizer,
  num_warmup_steps = 0,
  num_training_steps = total_steps
)
```
#### 학습 과정
```python
# 랜덤 시드 고정
seed_val = 42
random.seed(seed_val)
np.random.seed(seed_val)
torch.manual_seed(seed_val)
torch.cuda.manual_seed_all(seed_val)

# 그래디언트 초기화
model.zero_grad()

# 학습
for epoch_i in range(0, epochs):
  ...
  model.train() # 훈련 모드로 변경
  
  for step, batch in enumerate(train_dataloader):
    ...
    # forward 수행
    outputs = model(b_input_ids, token_type_ids=None, attention_mask= b_input_mask, labels=b_labels)
    # loss 구함
    loss = outputs[0]
    ...
    # backward 수행 > gradient 계산
    loss.backward()
    # 그래디언트 클리핑
    torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
    # 그래디언트를 통해 가중치 파라미터 업데이트
    optimizer.step()
    # 스케줄러로 학습률 감소
    scheduler.step()
    ...
    
    # === Validation ===
```

### 평가 및 결과
```python
...
model.eval() # 평가 모드로 변경
...
for step, batch in enumerate(test_dataloader):
  ...
  # gradient 계산 안하고 forward만 수행
  with torch.no_grad():
    outputs = model(b_input_ids, token_type_ids=None, attention_mask=b_input_mask)
  
  ...
```
```text
Accuracy: 0.83
Test took: 0:00:04
```

#### 새로운 문장 테스트
```python
def convert_input_data(sentences):
  tokenized_texts = [tokenizer.tokenize(sent) for sent in sentences]
  MAX_LEN = 128
  ...
  
  inputs = torch.tensor(input_ids) # data를 가진 token-ids를 tensor로
  masks = torch.tensor(attention_masks) # attention mask to tensor
  
  return inputs, masks 
```
```python
def test_sentences(sentences):
  model.eval() # 평가모드로 변경
  inputs, masks = convert_input_data(sentences)
  ...
  # 그래디언트 계산 X
  with torch.no_grad():
    # forward 수행
    outputs = model(b_input_ids, token_type_ids=None, attention_mask=b_input_mask)
  
  logits = outputs[0] # 로스 구함
  logits = logits.detach().cpu().numpy() # cpu로 데이터 이동
  return logits
```
```python
logits = test_sentences(['ㅅㅂ놈의 새끼들이 담배 존나 피네 시발 지 방안에서 나 피지:)'])
print(logits)

if np.argmax(logits) == 1 :
    print("비속어 입니다.")
elif np.argmax(logits) == 0 :
    print("비속어가 아닙니다.")
```
```text
[[-2.2733462  2.191932 ]]
비속어 입니다.
```

## 참고자료
- https://medium.com/analytics-vidhya/understanding-the-bert-model-a04e1c7933a9
- https://velog.io/@seolini43/%EC%9D%BC%EC%83%81%EC%97%B0%EC%95%A0-%EC%A3%BC%EC%A0%9C%EC%9D%98-%ED%95%9C%EA%B5%AD%EC%96%B4-%EB%8C%80%ED%99%94-BERT%EB%A1%9C-%EC%9D%B4%EC%A7%84-%EB%B6%84%EB%A5%98-%EB%AA%A8%EB%8D%B8-%EB%A7%8C%EB%93%A4%EA%B8%B0%ED%8C%8C%EC%9D%B4%EC%8D%ACColab-%EC%BD%94%EB%93%9C#2train-set--test-set%EC%9C%BC%EB%A1%9C-%EB%82%98%EB%88%84%EA%B8%B0