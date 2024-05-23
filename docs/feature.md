## Development Process
- Requirements -> Planning -> Design -> Implementation and Testing -> Deployment
  - Requirements : User Stories
  - Planning : GitHub Issues
  - Design : API-First Approach
  - Implementation and Testing : TDD
  - Deployment : CI/CD

## Requirements
#### As a `<user role>`, I want `<goal>`, so that `<rationale>`.
- 게스트는 회원가입을 할 수 있다.
- 로그인한 사용자가 사용자 닉네임으로 친구 추가를 한다.
- 로그인한 사용자가 친구 추가 허가를 한다.
- 로그인한 사용자가 친구에게 1:1 대화를 걸 수 있다.
- 로그인한 사용자는 오픈 채팅방을 만들 수 있다.
  - 오픈 채팅방을 만든 사용자는 해당 방의 관리자가 된다.
  - 오픈 채팅방 관리자는 그 방의 모든 사용자의 채팅 내용을 가리기 할 수 있다.
  - 오픈 채팅방 관리자는 특정 사용자를 강퇴할 수 있다.
- 로그인한 사용자는 오픈 채팅방에 들어갈 수 있다.
- 로그인한 사용자는 오픈 채팅방에서 나올 수 있다.

## 구현 기능 목록

