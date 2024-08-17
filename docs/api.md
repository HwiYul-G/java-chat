# 채팅 앱 API 문서
개정이력

|버전|변경일|변경사유|변경내역|
|:--:|:--:|:--:|:--:|
|1.0|2024-07-22|최초작성|최초작성|
|||||
|||||

## 데이터 공통 사항

## API 목록
- 로그인: POST `api/v1/auth/login`
    - email, password를 넘긴다.
    - userInfo(현 사용자 id, imgUri, email, 이름, enabled, roles) + token을 돌려준다.
- 회원가입: POST `api/v1/user` > 
    - email, username, password 등을 넘긴다.
    - userDto를 돌려준다.
- 비밀번호 찾기: ``
- 사용자 정보 수정: PUT `api/v1/user/{userId}`
    - id와 변화시킬 정보를 보낸다.
    - 변화된 정보값을 돌려준다.
- 회원 탈퇴: DELETE `api/v1/user/{userId}`
    - id를 보낸다.

- 그룹 채팅방 만들기: POST `api/v1/chat-rooms`
    - 채팅방의 '이름'을 넘김
    - 채팅방의 id 값을 받음
- 그룹 채팅방 참여하기: POST `api/v1/chat-join`
    - 채팅 방의 id 값을 넘김

- 친구 목록 조회: GET `api/v1/users/{userId}/friends`
- 친구 추가(초대): POST `api/v1/users/invite-friend`
- 친구 요청 수락: POST `api/v1/users/friend-requests/{friendRequestId}/accept`
- 친구 요청 거절: DELETE `api/v1/users/friend-requests/{friendRequestsId}`
- 친구 초대 받은 목록 조회: GET `api/v1/users/{userId}/request-friends`

- 개인 채팅 목록 조회: `api/v1/personal-chat-rooms/users/{userId}`
    - 나의 userId를 넘겨줌
    - imageUri, 친구 이름, 마지막에 보낸 메시지, roomId를 각 값으로 알아야함
    - 이때 마지막에 보낸 메시지는 계속 업데이트 될 수 있음
- 그룹 채팅 목록 조회: `api/v1/group-chat-rooms/users/{userId}`
    - 나의 userId를 넘겨준다.
    - 방이름, roomId, 마지막에 보낸 메시지를 각 값으로 알아야함.
    - 이때 마지막에 보낸 메시지는 계속 업데이트 될 수 있음.

### 로그인
#### 요청url
#### Request Parameters
#### Response Body

### 친구 목록 조회
#### 요청url
GET `api/v1/users/{userId}/friends`
#### Request Parameters
- query param : userId
#### Response Body
```json
{
    userId: ,
    name:,
    email:,
    roomId: ,
}
```
### 친구 추가(초대)
#### 요청 url
POST `api/v1/users/invite-friend`
#### Request Parameters
```json
{
    userId:,
    friendEmail:,
}
```
#### Response Body
```json
{

}
```
### 친구 요청 수락
#### 요청 uri
POST: `api/v1/users/friend-requests/{friendRequestId}/accept`
#### Request Parameters
#### Response Body 
```json
```

### 친구 요청 거절
#### 요청 uri
DELETE: `api/v1/users/friend-requests/{friendRequestId}`
#### Request Parameters
#### Response Body 
```json
```

### 친구 초대 받은 목록 조회
#### 요청 uri
GET: `api/v1/users/{userId}/request-friends`
#### Request Parameters
#### Response Body 
```json
{
    id:,
    senderName:,
    createdAt:
}
```