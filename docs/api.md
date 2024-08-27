# 채팅 앱 API 문서

### 개정이력
|버전|변경일|변경사유|변경내역|
|:--:|:--:|:--:|:--:|
|1.0|2024-07-22|최초작성|최초작성|
|||||
|||||

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
    "data": {...}
}
```

## API 목록
- [회원가입](#회원가입)
- 사용자 정보 업데이트: PUT `/users/${userId}`
- 계정 탈퇴(삭제)하기: " DELETE `/users/${userId}`
- email로 친구 찾기(사용자 찾기): GET `/users/email`
- 친구 목록 조회: GET`/users/${userId}/friends`
- 친구 초대 하기: POST `/users/invite-friend`
- 친구 초대 수락하기: POST `/users/friend-requests/${friendRequestId}/accept`
- 친구 초대 거절하기: DELETE `users/friend-requests/${friendRequestId}`
- 친구 초대 요청 목록 보기: GET `users/${userId}/request-friends`

- 로그인: POST `/auth/login`

- 그룹 채팅방 들어가기: POST `/group-chat-join`
- 그룹 채팅방 만들기: POST `/group-chat-rooms`
- 특정 그룹 채팅방의 메시지 조회:  GET `/group-chat-rooms/${roomId}/messages`
- 그룹 채팅방 목록 조회: GET`/group-chat-rooms/users/${userId}`
 
- 개인 채팅방 만들기: GET `/personal-chat-rooms`
- 특정 개인 채팅방의 메시지 조회: GET `/personal-chat-rooms/users/${userId}`
- 개인 채팅방 목록 조회: POST `/personal-chat-rooms`

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
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|email| string| |O||
|username|string||O|
|roles|string||O|- admin과 user라는 역할이 있다.</br>- 두 역할을 줄 때는 `admin user`으로 공백으로 구분한다.|
|enabled| boolean||X|- 기본적으로 `true`</br> - `false`인 경우 block처리 된 것을 의미한다.|

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
#### 요청 URL
- `api/v1/users/${userId}`
- HTTP Method Type: `PUT`
#### Path Parameters
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId| Long | 사용자 고유 id| O ||
#### Request Body
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|id|Long||X||
|email|string||O||
|username|string||O||
|roles|string||O||
|enabled|boolean||X||

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
- `api/v1/users/${userId}`
- HTTP Method Type: `DELETE`
#### Path Parameters
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId| Long | 사용자 고유 id| O ||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "Delete Success",
}
```

### email로 친구 찾기(사용자 찾기)
#### 요청URL
- `api/v1/users/email`
- Http Method Type: `GET`
#### Request Parameters
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|email| String | | O ||

#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "Find One Success",
    "data": {
        "id": 1,
        "email": "a@google.com",
        "username": "a",
        "enabled": true,
        "roles": "user"
    }
}
```

### 친구 목록 조회
#### 요청URL
- `api/v1/users/${userId}/friends`
- Http Method Type: `GET`
#### Path Parameter
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId| Long | | O ||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "Find One Success",
    "data": [
        {
            "userId": 1,
            "name": "a",
            "email": "a@google.com",
            "chatRoomId": 2
        },
        {
            "userId": 2,
            "name": "b",
            "email": "b@google.com",
            "chatRoomId": 1
        },
        // ...
    ]
}
```

### 친구 초대 하기
#### 요청URL
- `api/v1/users/invite-friend`
- Http Method Type: `POST`
#### Request Body
```json
{
    "userId": 1,
    "friendEmail": "a@google.com"
}
```
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId|Long||O||
|friendEmail|String||O||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "친구 초대 요청 성공",
}
```

### 친구 초대 수락하기
#### 요청URL
- `api/v1/users/friend-requests/${friendRequestId}/accept`
- Http Method Type: `POST`
#### Path Parameter
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|friendRequestId| Long | | O ||

#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "친구 초대 요청 수락 성공",
}
```

### 친구 초대 거절하기
#### 요청URL
- `api/v1/users/friend-requests/${friendRequestId}`
- Http Method Type: `DELETE`
#### Path Parameter
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|friendRequestId| Long | | O ||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "친구 초대 요청 거절 성공",
}
```

### 친구 초대 요청 목록 보기
#### 요청URL
- `api/v1/users/${userId}/request-friends`
- Http Method Type: `GET`
#### Path Parameter
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId| Long | | O ||
#### Response Body
```json
    "flag": true,
    "code": 200,
    "message": "친구 초대 요청 거절 성공",
    "data": [
        {
            "id": 1,
            "senderId": 2,
            "receiverId": 1,
            "senderName": "b",
            "createdAt": "2023-08-27T13:30:25" // yyyy-MM-dd'T'HH:mm:ss 형식
        },
        // ...
    ]
```

### 로그인
#### 요청URL
- `api/v1/auth/login`
- Http Method Type: `POST`

### 그룹 채팅방 들어가기
#### 요청URL
- `api/v1/group-chat-join`
- Http Method Type: `POST`

### 그룹 채팅방 만들기
#### 요청URL
- `api/v1/group-chat-rooms`
- Http Method Type: `POST`

### 특정 그룹 채팅방의 메시지 조회
#### 요청URL
- `api/v1/group-chat-rooms/${roomId}/messages`
- Http Method Type: `GET`

### 그룹 채팅방 목록 조회하기
#### 요청URL
- `api/v1/group-chat-rooms/users/${userId}`
- Http Method Type: `GET`

### 개인 채팅방 만들기
#### 요청URL
- `api/v1/personal-chat-rooms`
- Http Method Type: `GET`

### 특정 개인 채팅방의 메시지 조회
#### 요청URL
- `api/v1/personal-chat-rooms/users/${userId}`
- Http Method Type: `GET`

### 개인 채팅방 목록 조회
#### 요청URL
- `api/v1/personal-chat-rooms`
- Http Method Type: `POST`