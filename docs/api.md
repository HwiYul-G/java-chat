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
- 회원가입: POST `api/v1/user`
- 비밀번호 찾기: ``
- 사용자 정보 수정: PUT `api/v1/user/{userId}`
- 회원 탈퇴: DELETE `api/v1/user/{userId}`
- 이메일로 사용자 찾기: GET `api/v1/user/email`

- 그룹 채팅방 만들기: POST `api/v1/chat-rooms`
- 그룹 채팅방 참여하기: POST `api/v1/chat-join`

- 친구 목록 조회: GET `api/v1/user/{userId}/friends`
- 친구 추가(초대): POST `api/v1/user/{userId}/friends`
- 친구 요청 수락 alc rjw

- 개인 채팅 목록 조회: `api/v1/user/{userId}/private-chats`
- 그룹 채팅 목록 조회: `api/v1/user/{userId}/group-chats`

- 알림 목록 조회: GET `api/v1/`

### 로그인
#### 요청url
#### Request Parameters
#### Response Body
