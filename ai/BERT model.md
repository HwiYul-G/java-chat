# BERT(Bidirectional Encoder Representations from Transformers)
BERT 언어 모델은 자연어 처리(NLP)를 위한 오픈 소스 머신 러닝 프레임워크이다. BERT는 위키피디아 텍스트로 사전학습되었고, **문맥, 맥락**을 이해한다.

- 목차
    1. [BERT의 핵심개념](#bert의-핵심-개념)
    2. [BERT 배경 및 역사](#bert-배경-및-역사)
    3. [BERT 동작 방법](#bert-동작-방법)

## BERT의 핵심 개념
### 맥락 기반 이해
BERT는 주변 텍스트를 사용해서 문맥을 이해하고, 문장에서 모호한
언어의 의미를 파악한다. `it`같은 대명사가 가리키는 것을 찾을 수 있다.
### Transformers 기반
transformer는 모든 결과 요소가 모든 입력 요소에 연결된 딥러닝 모델로, input과 output 요소들 사이의 가중치(weightings)는 연결에 따라 동적으로 계산된다.
### 양방향 처리(bidirectional)
이전의 언어 모델은 입력 텍스트를 순차적으로(한 방향으로)만 읽을 수 있다. 하지만 BERT는 동시에 양방향이 가능하다. 좌->우, 우->좌를 읽도록 설계되었다.

양방향 텍스트를 동시처리하는 것을 이용해 bert는 두 가지 주요 NLP 작업에서 사전 훈련되었다. 
- Masked Language Model(MLM)
  - 문장 내에서 일부 단어를 숨기고, 모델이 그 단어를 예측하도록 학습
- Next Sentence Prediction(NSP)
  - 주어진 두 문장이 논리적이고 순차적인 연결을 가지는 지, 단순히 이유없이 붙어있는지 예측한다. 즉 문장간의 관계를 파악한다.

## BERT 배경 및 역사
구글은 2017년에 Transformer model을 처음 소개했다.
이 당시 언어 모델은 대부분 Recurrent Neural Network(RNN)과 Convolutional Neural Networks(CNN)을 사용해서 NLP 작업을 했다.
### Transformer model의 장점
- **CNNs, RNNs 한계**: CNNs, RNNs는 component model로 data의 sequence를 필요로해서 고정된 순서로 처리한다.
- **Transformers model은 any order 처리**: Transformer models은 고정된 순서로 처리하는 data sequences가 필요 없다.

### 2018년
- 구글은 오픈 소스로 BERT를 공개했다. 
- 연구 단계에서 BERT는 11개의 언어를 이해하는 다양한 NLU(Natural Language Understanding) 작업에서 성과를 보였다. 
  - sentiment analysis, semantic role labeling, text classification, disambiguation of words(여러 의미를 가져서 모호한 단어들 처리)
- 다중어를 다루는 것이 BERT의 큰 성과이다.
  - BERT 이전 모델인 word2vec, GloVe는 맥락을 해석하고 여러 의미를 가진 단어를 이해하지 못했다. 반면, BERT는 효율적으로 모호함을 다루었다. 

### 2019년
- 구글은 BERT를 미국 검색 알고리즘에 적용하기 시작했다.
  - BERT는 미국 영어 기반의 구글 검색 쿼리를 이해하는 것의 약 10%를 개선
  - BERT를 통해 자연스러운 검색 경험을 제공했다
- 2019.12에 BERT는 70개의 다른 언어들로 확대 적용되었다.
  - 텍스트 검색뿐 아니라 음성 검색에도 영향을 주었다.
  - 이전 구글 NLP 기술에서 발견된 오류를 개선하며 검색 엔진 최적화를 크게 향상시켰다.
  - BERT의 문맥 이해 능력으로 여러 언어의 패턴을 해석하고 공유할 수 있게 되었다.

### 그 후 BERT의 영향
- bert는 많은 인공지능 시스템에 영향을 주었다. 
- bert의 다양하고 경량화된 버전과 유사한 훈련 메소드들이 gpt2에서 chatgpt까지 적용되어졌다.

## BERT 동작 방법
> NLP 기술의 목표는 인간의 언어를 자연스럽게 이해하는 것이다.
- BERT는 주어진 문장에서 빈칸을 예측해서 언어의 의미를 학습한다.
  - 대규모의 라벨링된 데이터셋을 학습하는 전통적인 방법X
  - 라벨링되지 않은 평문 텍스트만을 사용해 훈련되었다.
  - 비지도 학습으로 훈련되었고, google 검색처럼 실제로 사용됨
- BERT는 기본 지식을 제공하는 사전 학습을 통해 응답을 구축한다.
  - 사용자의 요구에 따라 파인 튜닝, 전이학습(transfer learning)될 수 있다.

### BERT의 핵심 기술
- Transformers, Masked Language Modeling(MLM), Self-Attention Mechanisms

#### Transformers
Transformer는 문장 내의 모든 단어 간 관계를 동시에 처리해 전체 문맥을 이해하는 데 도움을 준다. 이는 전통적인 단어 임베딩(Word Embedding) 방식과 달리, 단어의 의미를 고정된 벡터로 표현하지 않고 전체 문맥을 고려해 처리한다.

#### Masked Language Modeling(MLM)
전통적인 단어 임베딩 모델은 특정 단어에 고정된 벡터를 사용하지만, BERT는 MLM 기법을 사용해 문맥에 기반해 단어를 예측한다. BERT는 문맥에서 숨겨진 단어를 예측하고, 각 단어는 주변 단어들에 의해 정의된다. 즉 고정된 의미가 아니라 문맥에 따라 단어의 의미를 파악한다.

#### Self-Attention Mechanisms
BERT는 **Self-Attention Mechanism**을 사용하여 문장 내 단어들 간의 관계를 포착하고 이해한다. BERT의 bidirectional transformers는 각 단어의 의미를 문장 전체의 문맥에서 파악할 수 있게 한다. 이로 인해 문장 내 단어의 의미가 문맥에 따라 변화하는 것을 효과적으로 처리할 수 있습니다.

## 참고 자료
- [BERT language model](https://www.techtarget.com/searchenterpriseai/definition/BERT-language-model)
- [트랜스포머](https://wikidocs.net/31379)