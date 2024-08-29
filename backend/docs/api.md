# 채팅 앱 API 문서

### 개정이력
|버전|변경일|변경사유|변경내역|
|:--:|:--:|:--:|:--|
|1.0|2024-07-22|최초작성|최초작성|
|1.1|2024-08-28|구체화| - 각 API의 request, response 구체적 작성</br> - 데이터 공통 사항 추가</br>|
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
    "data": {
        // ...
    }
}
```
### MessageType
- `PersonalChat`과 `GroupChat`관련된 message Type 종류는 다음과 같다.
|type|description|
|:--:|:--:|
|CONNECT| 연결|
|DISCONNECT|연결 해제|
|CHAT|일반 채팅 메시지|
|DATE|날짜와 관련된 정보 메시지|


## API 목록
- [회원가입](#회원가입)
- [사용자 정보 업데이트](#사용자-정보-업데이트)
- [계정 탈퇴(삭제)하기](#계정-탈퇴삭제하기)
- [email로 친구 찾기(사용자 찾기)](#email로-친구-찾기사용자-찾기)
- [친구 목록 조회](#친구-목록-조회)
- [친구 초대 하기](#친구-초대-하기)
- [친구 초대 수락하기](#친구-초대-수락하기)
- [친구 초대 거절하기](#친구-초대-거절하기)
- [친구 초대 요청 목록 보기](#친구-초대-요청-목록-보기)
- [로그인](#로그인)
- [그룹 채팅방 들어가기](#그룹-채팅방-들어가기)
- [그룹 채팅방 만들기](#그룹-채팅방-만들기)
- [특정 그룹 채팅방의 메시지 조회](#특정-그룹-채팅방의-메시지-조회)
- [그룹 채팅방 목록 조회](#그룹-채팅방-목록-조회하기)
- [개인 채팅방 만들기](#개인-채팅방-만들기)
- [특정 개인 채팅방의 메시지 조회](#특정-개인-채팅방의-메시지-조회)
- [개인 채팅방 목록 조회](#개인-채팅방-목록-조회)

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
{
    "flag": true,
    "code": 200,
    "message": "친구 초대 요청 목록 조회 성공",
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
}
```

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
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|username|string||O|`email`을 넘겨야한다.|
|password|string||O||
#### Header Parameters
| Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|Authorization|string|Basic Authentication|O|`Basic base64(username:password)`형태의 포맷|

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
            "role": "admin user", 
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

### 그룹 채팅방 들어가기
#### 요청URL
- `api/v1/group-chat-join`
- Http Method Type: `POST`
#### Request Body
```json
{
    "userId": 1,
    "roomId": 3
}
```
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId|Long||O|채팅에 조인하는 사용자 id|
|roomId|Long||O|조인할 채팅 방 id|
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅 조인 성공",
    "data": {
        "id": 2,
        "userId": 1,
        "roomId": 3,
        "createdAt": "2024-08-28T10:19:25" 
    }
}
```

### 그룹 채팅방 만들기
#### 요청URL
- `api/v1/group-chat-rooms`
- Http Method Type: `POST`
#### Request Body
```json
{
    "name": "채팅방 이름",
    "managerUserId": 2
}
```
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅방 생성 성공",
    "data": {
        "id": 2,
        "name": "채팅방 이름",
        "createdAt":"2024-08-24T11:20:11",
        "managerUserId": 2
    }
}
```

### 특정 그룹 채팅방의 메시지 조회
#### 요청URL
- `api/v1/group-chat-rooms/${roomId}/messages`
- Http Method Type: `GET`
#### Path Parameter
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|roomId|Long||O||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅 리스트 조회 성공",
    "data": [ 
        {
            "id": 2,
            "senderId": 2,
            "roomId": 3,
            "senderName": "b",
            "content": "메시지 내용",
            "createdAt": "2024-11-11T11:11:12",
            "type": "CHAT"
        },
        // ,,,
    ]

}
```

### 그룹 채팅방 목록 조회하기
#### 요청URL
- `api/v1/group-chat-rooms/users/${userId}`
- Http Method Type: `GET`
#### Path Parameters
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId|Long||O||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅방 목록 조회 성공",
    "data": [
        {
            "groupChatRoomId": 1,
            "groupChatRoomName": "채팅방 이름",
            "lastMessage": "마지막에 보낸 메시지"
        },
        // ...
    ]
}
```
### 개인 채팅방 만들기
#### 요청URL
- `api/v1/personal-chat-rooms`
- Http Method Type: `GET`
#### Request Body
```json
{
    "userId1": 1,
    "userId2": 2
}
```
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId1|Long||O|현재 로그인한 사용자 Id로 보낸다.|
|userId2|Long||O|친구의 id로 보낸다.|
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅방 생성 성공",
    "data": {
        "id": 1,
        "userId1": 1,
        "userId2": 2
    }
}
```

### 특정 개인 채팅방의 메시지 조회
#### 요청URL
- `api/v1/personal-chat-rooms/${roomId}/messages`
- Http Method Type: `POST`
#### Path Parameter
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|roomId|Long||O||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "채팅 리스트 조회 성공",
    "data": [
        {
            "id": 111,
            "senderId": 2,
            "content" : "채팅 메시지 내용",
            "createdAt": "2024-08-28T11:11:11",
            "type": "CHAT",
        },
        // ...
    ]
}
```

### 개인 채팅방 목록 조회
#### 요청URL
- `api/v1/personal-chat-rooms/users/${userId}`
- Http Method Type: `GET`
#### Path Parameter
| Field Name | Type | Description | Mandatory | Note |
|:--:|:--:|:--:|:--:|:--|
|userId|Long||O||
#### Response Body
```json
{
    "flag": true,
    "code": 200,
    "message": "개인 채팅방 목록 조회 성공",
    "data": [
        {
            "roomId": 1,
            "friendId": 2,
            "friendName": "b",
            "friendEmail": "b@google.com",
            "lastMessage": "마지막으로 보낸 메시지"
        },
        // ...
    ]
}
```