# 채팅 앱 API 문서

### 개정이력

| 버전  |    변경일     |     변경사유      | 변경내역                                                       |
|:---:|:----------:|:-------------:|:-----------------------------------------------------------|
| 1.0 | 2024-07-22 |     최초작성      | 최초작성                                                       |
| 1.1 | 2024-08-28 |      구체화      | - 각 API의 request, response 구체적 작성</br> - 데이터 공통 사항 추가</br> |
| 2.0 | 2024-09-04 | 데이터베이스 스키마 변경 | - 엔드포인트 통합(API 통합)</br>                                    |
|     |            |               |                                                            |

## 데이터 공통 사항

### Response

응답은 아래와 같은 규칙을 따른다.

- JSON 포맷의 데이터
- 필드명은 소문자로 시작하는 `camel notation`을 사용한다.
- `data`필드는 존재하지 않을 수도 있다.

```json
{
  "flag": true,
  "code": 200,
  "message": "Add Success",
  "data": {
    // ...
  }
}
```

### MessageType

- `ChatMessage`와 관련된 `messageType` 종류는 다음과 같다.
  |type|description|
  |:--:|:--:|
  |CONNECT| 연결|
  |DISCONNECT|연결 해제|
  |CHAT|일반 채팅 메시지|
  |DATE|날짜와 관련된 정보 메시지|

### Notification 관련
#### Notification Status
- 결정 상태가 필요하지 않은 것은 `NONE` 을 가진다.
- 결정 상태가 있는 것은 `PENDING`으로 시작해서 `ACCEPTED`나 `DECLIEND`로 변한다.

|  status  | description |
|:--------:|:-----------:|
| PENDING  |   결정x 상태    |
| ACCEPTED |     수락      |
| DECLINED |     거절      |
|   NONE   | 결정과 무관한 알림  |

#### Notification Type

|      type      | description |
|:--------------:|:-----------:|
| FRIEND_REQUEST |    친구 요청    |

## API 목록
- [로그인](#로그인)
- [회원가입](#회원가입)
- [사용자 정보 업데이트](#사용자-정보-업데이트)
- [계정 탈퇴(삭제)하기](#계정-탈퇴삭제하기)
- [친구 목록 조회](#친구-목록-조회)
- [알림 목록 조회](#알림-목록-조회)
- [채팅방 목록 조회](#채팅방-목록-조회)
- [친구 추가 알림 생성](#친구-추가-알림-생성)
- [알림 상태 변경](#알림-상태-변경)
- [특정 채팅방의 메시지 조회](#특정-채팅방의-메시지-조회)
- [채팅방 생성](#채팅방-생성)
- [그룹 채팅방 들어가기](#그룹-채팅방-들어가기)

### 로그인
#### 요청URL
- `api/v1/auth/login`
- Http Method Type: `POST`
#### Request Body Parameter
**Content Type: `application/x-www-form-urlencoded`**
```json
{
  "username": "a@google.com",
  "password": "12345abcdefg"
}
```
| Field Name |  Type  | Description | Mandatory | Note            |
|:----------:|:------:|:-----------:|:---------:|:----------------|
|  username  | string |             |     O     | `email`을 넘겨야한다. |
|  password  | string |             |     O     |                 |

#### Header Parameters
|     Name      |  Type  |     Description      | Mandatory | Note                                    |
|:-------------:|:------:|:--------------------:|:---------:|:----------------------------------------|
| Authorization | string | Basic Authentication |     O     | `Basic base64(username:password)`형태의 포맷 |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "User Info and JSON Web Token",
  "data": {
    "userInfo": {
      "id": 1,
      "username": "a",
      "email": "a@google.com",
      "enabled": true,
      "role": "admin user"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

<details>
    <summary>자바스크립트 요청 예시</summary>

    ```javascript
    import instance from ".";
    import qs from "qs";

    const prefix = '/auth';

    export const login = async ({email, password}) => {
        const data = {username: email, password: password};
        try{
            const res = await instance.post(`${prefix}/login`, qs.stringify(data), {
                auth: data,
            })
        }catch(err){
            console.error("서버의 로그인 API 호출 실패: ", err);
            throw err;
        }
    };
    ```

</details>

### 회원가입
#### 요청URL
- `api/v1/users`
- HTTP Method Type: `POST`

#### Request Body
```json
{
  "email": "a@google.com",
  "username": "a",
  "password": "abcdefg12345",
  "roles": "user"
}
```

| Field Name |  Type   | Description | Mandatory | Note                                                               |
|:----------:|:-------:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   email    | string  |             |     O     |                                                                    |
|  username  | string  |             |     O     |
|   roles    | string  |             |     O     | - admin과 user라는 역할이 있다.</br>- 두 역할을 줄 때는 `admin user`으로 공백으로 구분한다. |
|  enabled   | boolean |             |     X     | - 기본적으로 `true`</br> - `false`인 경우 block처리 된 것을 의미한다.               |

#### Response Body

```json
{
  "flag": true,
  "code": 200,
  "message": "Add Success",
  "data": {
    "id": 1,
    "email": "a@google.com",
    "username": "a",
    "enabled": true,
    "roles": "user"
  }
}
```

### 사용자 정보 업데이트
#### 요청URL
- `api/v1/users/{userId}`
- HTTP Method Type: `PUT`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                                    |

#### Request Body
```json
{
  "email": "a@google.com",
  "username": "a",
  "roles": "user"
}
```
| Field Name |  Type   | Description | Mandatory | Note                                                               |
|:----------:|:-------:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   email    | string  |             |     O     |                                                                    |
|  username  | string  |             |     O     |
|   roles    | string  |             |     O     | - admin과 user라는 역할이 있다.</br>- 두 역할을 줄 때는 `admin user`으로 공백으로 구분한다. |
|  enabled   | boolean |             |     X     | - 기본적으로 `true`</br> - `false`인 경우 block처리 된 것을 의미한다.               |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "Update Success",
  "data": {
    "id": 1,
    "email": "a@google.com",
    "username": "a",
    "enabled": true,
    "roles": "user"
  }
}
```

### 계정 탈퇴(삭제)하기
#### 요청URL
- `api/v1/users/{userId}`
- HTTP Method Type: `DELETE`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                                    |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "Delete Success"
}
```

### 친구 목록 조회
#### 요청URL
- `api/v1/users/{userId}/friends`
- HTTP Method Type: `Get`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                                    |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "친구 찾기 성공",
  "data": [
    {
      "userId": 1,
      "name": "a",
      "email": "a@google.com",
      "chatRoomId": 1 // 대화를 한 번도 하지 않은 경우 null일 수 있다.
    },
    // ...
  ]
}
```

### 알림 목록 조회
#### 요청URL
- `api/v1/users/{userId}/notifications`
- HTTP Method Type: `Get`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                                    |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "알림 목록 조회 성공",
  "data": [
    {
      "id": 1,
      "content":"알림 내용",
      "isRead": false,
      "createdAt": "2024-09-03T11:11:11",
      "status": "PENDING",
      "type": "FRIEND_REQUEST"
    },
    // ...
  ]
}
```

### 채팅방 목록 조회
#### 요청URL
- `api/v1/users/{userId}/chat-rooms`
- HTTP Method Type: `Get`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                                    |

#### Response Body

```json
{
  "flag": true,
  "code": 200,
  "message": "채팅방 목록 조회 성공",
  "data": [
    {
      "roomId": 1,
      "isGroup": false,
      "chatRoomInfo": {
        "roomName": "그룹채팅방이름",
        "managerId": 2
      },
      "friendInfo": {
        "name": "",
        "email": "", // 그룹채팅방 정보가 있을 때는 null이 될 것
      },
      "lastMessageInfo": {
        "content": "마지막 메시지 내용",
        "createdAt": "2024-09-11T11:11:11"
      }
    }
    // ...
  ]
}
```

### 친구 추가 알림 생성
#### 요청URL
- `api/v1/notifications/friend-invitation`
- HTTP Method Type: `POST`

#### Request Body
```json
{
  "userId": 1,
  "friendEmail": "b@google.com"
}
```
| Field Name  |  Type  | Description | Mandatory | Note                                                               |
|:-----------:|:------:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   userId    |  Long  |  사용자(나) id  |     O     |                                                                    |
| friendEmail | string |             |     O     |


#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "알림 생성 성공",
  "data": {
      "id": 1,
      "content":"알림 내용",
      "isRead": false,
      "createdAt": "2024-09-03T11:11:11",
      "status": "PENDING",
      "type": "FRIEND_REQUEST"
    }
}
```

### 알림 상태 변경
#### 요청URL
- `api/v1/notifications/{notificationId}`
- HTTP Method Type: `PUT`
- 예시 주소 `api/v1/notifications/{notificationId}?isAccept=true`

#### Path Parameter
|   Field Name   | Type | Description | Mandatory | Note                                                               |
|:--------------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
| notificationId | Long |             |     O     |                                                                    |

#### Request Parameter
| Field Name |  Type   | Description | Mandatory | Note                      |
|:----------:|:-------:|:-----------:|:---------:|:--------------------------|
|  isAccept  | boolean |             |     O     | `true`이면 승락, `false`이면 거절 |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "알림 상태 변경 성공",
  "data": {
      "id": 1,
      "content":"알림 내용",
      "isRead": false,
      "createdAt": "2024-09-03T11:11:11",
      "status": "ACCEPT",
      "type": "FRIEND_REQUEST"
    }
}
```

### 특정 채팅방의 메시지 조회
#### 요청URL
- `api/v1/chat-rooms/{roomId}/messages`
- HTTP Method Type: `GET`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   roomId   | Long |             |     O     |                                                                    |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "채팅 메시지 목록 조회 성공",
  "data": [
    {
      "id": 1,
      "roomId": 2,
      "senderId": 2,
      "senderName": "b",
      "content": "메시지에 보낸 내용",
      "createdAt": "2011-11-11T11:11:11",
      "type": "CHAT"
    },
    // ...
  ]
}
```

### 채팅방 생성
#### 요청URL
- `api/v1/chat-rooms`
- HTTP Method Type: `POST`

#### Request Body| Field Name | Type | Description | Mandatory | Note                                                        |
|:----------:|:----:|:-----------:|:---------:|:------------------------------------------------------------|
|   isGroup   | Long |             |     O     |                                                             |
|   userId   | Long |             |     O     |                                                             |
|   roomName   | Long |             |     X     |       그룹채팅방인 경우 필요한 인자(그룹의 경우 필수)                                                      |
|   friendId   | Long |             |     X     |                                              개인 채팅방인 경우 필요한인자(개인인 경우 필수)               |

##### 그룹 채팅방 생성인 경우
```json
{
  "isGroup": true,
  "userId": 1,
  "roomName": "그룹 채팅방일때 필요한 방이름"
}
```
##### 개인 채팅방 생성인 경우
```json
{
  "isGroup": true,
  "userId": 1,
  "friendId": 2
}
```

#### Response Body
##### 그룹 채팅방인 경우
```json
{
  "flag": true,
  "code": 200,
  "message": "그룹 채팅방 생성 성공",
  "data":{
    "roomId": 1,
    "isGroup": false,
    "chatRoomInfo": {
      "roomName": "그룹채팅방이름",
      "managerId": 2
    },
    "lastMessageInfo": {
      "content": "마지막 메시지 내용",
      "createdAt": "2024-09-11T11:11:11"
    }
  }
}
```
#### 개인 채팅방인 경우
```json
{
  "flag": true,
  "code": 200,
  "message": "개인 채팅방 생성 성공",
  "data":{
    "roomId": 1,
    "isGroup": false,
    "friendInfo": {
      "name": "b",
      "email": "b@google.com"
    },
    "lastMessageInfo": {
      "content": "마지막 메시지 내용",
      "createdAt": "2024-09-11T11:11:11"
    }
  }
}
```

### 그룹 채팅방 들어가기
#### 요청URL
- `api/v1/chat-rooms/{roomId}/enter`
- HTTP Method Type: `POST`

#### Path Parameter
| Field Name | Type | Description | Mandatory | Note                                                               |
|:----------:|:----:|:-----------:|:---------:|:-------------------------------------------------------------------|
|   roomId   | Long |             |     O     |                                                                    |

#### Request Body
```json
{
  "userId": 1,
  "isGroup": true
}
```
| Field Name | Type | Description | Mandatory | Note                                                        |
|:----------:|:----:|:-----------:|:---------:|:------------------------------------------------------------|
|   userId   | Long |             |     O     |                                                             |
|isGroup|boolean||     O     | - 그룹 채팅방 들어가기 이므로 사실상 `true`</br> - 추후 기능 확장 가능성을 위해 받는 인자. |

#### Response Body
```json
{
  "flag": true,
  "code": 200,
  "message": "그룹 채팅방 들어가기 성공",
  "data": {
    "roomId": 1,
    "isGroup": false,
    "chatRoomInfo": {
      "roomName": "그룹채팅방이름",
      "managerId": 2
    },
    "lastMessageInfo": {
      "content": "마지막 메시지 내용",
      "createdAt": "2024-09-11T11:11:11"
    }
  }
}
```