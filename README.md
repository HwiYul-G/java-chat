# 채팅 서비스
Bert를 이용한 비속어 탐지 AI가 적용된 실시간 양방향 채팅 서비스

이 포트폴리오는 생성형 AI를 사용하지 않았습니다.

## Demo

### 스크린샷

|                                    로그인                                    |                                        알람                                        |
|:-------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img src="./frontend/docs/resources/login.png" width="400" height="200"/> | <img src="./frontend/docs/resources/notification.png" width="400" height="200"/> |

|                                    친구초대                                    |                                     회원가입                                     |
|:--------------------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| <img src="./frontend/docs/resources/invite.png" width="400" height="200"/> | <img src="./frontend/docs/resources/register.png" width="400" height="200"/> |

### 채팅 영상

<img src="./frontend/docs/resources/chat_movie.gif"/>

### 비속어 감지
- 사용자들이 보낸 메시지가 채팅방에 보내진다.
- 약 10초의 시간동안 비속어 여부를 판별 후
  - 비속어인 경우 메시지가 가려진다. `비속어가 탐지되어 메시지가 가려졌습니다.` 로 업데이트 된다.

<img src="./frontend/docs/resources/badword_detection_20sec.gif"/>

### docs

|       제목       |                        파일                        |
|:--------------:|:------------------------------------------------:|
|     API 문서     |           [📑](./backend/docs/api.md)            |
| AI 사용 데이터 및 학습 |    [📖](./ai/README.md)    |
|  AI 실제 학습 코드   | [📑](./ai/BERT_korean_profanity_detection.ipynb) |
|                |                                                  |

## Steps to Setup

### Local
#### Requirements
`java - 17`, `maven - 4.0.0`, `node - 20.13.1`, `Azurite`

#### AI

[🔗 비속어 탐지 onnx 모델 다운로드 링크](https://github.com/HwiYul-G/java-chat/releases)</br>
위 파일을 다운로드 후 AI 서버의 spring-boot의 resources에 파일명을 `model.onnx`로 변경해 넣는다.
```bash
azurite --silent --location c:\azurite --debug c:\azurite\debug.log
```
- azurite 실행 후, `input-queue`와 `output-queue` 생성(이름 그대로 사용)
```bash
mvn spring-boot:run
```

#### Backend

```bash
mvn spring-boot:run
```

#### Frontend

```bash
npm install
npm start
```

```
http://localhost:3000/
```

## workflow & architecture
<img src="./backend/docs/resources/workflow%20and%20architecture.png" width="544" height="328" alt="cicd worflow & architecture image">


## 학습 내역

### AI

| 제목          |            학습내역            |
|:------------|:--------------------------:|
| BERT 모델 소개  | [📖](./ai/BERT%20model.md) |
| 사용 데이터 및 학습 |    [📖](./ai/README.md)    |
|             |                            |

### backend

| 제목                                                                  |                   학습 내역                    |
|:--------------------------------------------------------------------|:------------------------------------------:|
| 비동기 처리와 약한 결합을 위한 `event`학습 - event, publish, listeners             |    [📖](./backend/docs/study/event.md)     |
| JWT authentication 구현 마스터하기!                                        |    [📖](./backend/docs/study/token.md)     |
| 채팅 구현을 위한 STOMP 사용 방법                                               | [📖](./backend/docs/study/spring-stomp.md) |
| Spring WebFulx - sync, async와 blocking, nonblocking부터 webflux 사용법까지 |   [📖](./backend/docs/study/webflex.md)    |
|                                                                     |                                            |

### frontend

| 제목                                           |                         학습 내역                          |
|:---------------------------------------------|:------------------------------------------------------:|
| react에서 api 사용하기 - fetch, axios              |      [📖](./frontend/docs/study/consume-apis.md)       |
| react에서 navigation하기 - react router dom 사용법  |         [📖](./frontend/docs/study/router.md)          |
| context API로 props drilling 해결 및 data shring |         [📖](./frontend/docs/study/context.md)         |
| websocket의 필요와 connection                    | [📖](./frontend/docs/study/websocket-and-filtering.md) |
|                                              |                                                        |

---
이 문서와 코드는 [KSY(HwiYul-G)](https://github.com/HwiYul-G)에 의해 작성되었습니다. 클론 시 꼭 표기 부탁드립니다. </br>
This document and code were written by [KSY(HwiYul-G)](https://github.com/HwiYul-G). Please be sure to indicate this
when cloning

Copyright (c) 2024. Soyeon Kim(Hwiyul_G). All rights reserved.
