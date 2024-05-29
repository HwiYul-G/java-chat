## Authentication and Authorization
- Authentication
  - 너는 누구냐? 인증
- Authorization
  - 너는 이거 할 자격이 있냐? 권한

## Spring Security
- HTTP basic authentication과 Spring Data JPA를 사용한 Username과 password authentication
- Spring Security OAuth2 Resource Server를 사용한 JSON web token(JWT) 생성과 validation
- Role-based authorization 혹은 access control
- Authentication과 Authorization Exception

## Athentication Process
1. username과 password를 사용자가 입력한다.
2. http basic authentication을 사용해서 데이터베이스의 정보로 authenticate한다.
    - AuthenticationProvider로 username과 password가 전달된다.
    - DB에 있는 정보와 비교해 옳은지 판단하기 위해 loadUserByUsername을 호출한다.
    - loadUserByUsername은 userService가 가진 메소드이다.
    - userService는 loadUserByUsername을 위해서 UserRepository를 이용해서 DB의 정보에 접근한다.
    - Optional<User>를 가져와 userService로 보낸다.
    - userService는 MyUserPrincipal을 AuthenticationProvider에게 돌려준다.
    - 여기서 Authentication이 성공한다.
    - 결론
      - UserService는 Spring Security의 인터페이스를 구현해야 한다. (loadUserByUsername 메소드)
      - Spring Security의 인터페이스를 구현한 클래스에서 User object는 wrap되어서 리턴되야한다.
3. success하면 jwt를 생성한다.
    - authentication의 과정이 성공하면, AuthController로 getLoginInfo(authentication)을 보낸다.
    - AuthController는 getLoginInfo를 위해서 AuthService의 createLoginInfo를 호출한다.
    - AuthService는 jwtProvider의 createToken을 호출한다.
    - jwtProvider는 AuthService에 JSON web token을 보낸다.
    - AuthService는 AuthController로 User info와 JWT를 보낸다.
    ```JSON
    {
      "flag": true,
      "code": 200, 
      "message": "메시지",
      "data": {
        "userInfo": {
          "id": 1,
          "email": "example@example.com",
          "username": "exam",
          "enabled": true,
          "roles": "admin"
        }  
       },
      "token": "~~~~~~~~~~~~~"
    }  
    ```