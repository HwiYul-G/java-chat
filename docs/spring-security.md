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
<br><br>
- 스프링 시큐리티는 기본적으로 아래의 것이 설정되어 있다.
  - application과 상호작용을 위해서 인증된 사용자를 요구한다.
  - 애플리케이션에 기본 로그인 폼을 생성한다.
  - 'user'라는 username과 폼기반 인증을 위해서 콘솔로 로그인하는 password를 가진 인메모리 사용자를 생성한다. 


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
   
## HTTP Basic Authentication
- 클라이언트는 Authorization request header 안에 credentials(자격정보)를 제공한다.
  - headers는 `Basic`이란 단어 우측으로 스페이스된 것을 포함한다.
  - headers는 base64-encoded인 username:password string을 포함한다. 예를 들어 a:123456
  - 위의 자격조건(credential)을 위해서, Authorization request header는 `Basic asdfasdfadf=`같은 것이 된다.
- httpBasic()을 필터에 달아준다. Authorization request header가 전달한 값을 사용해서 request를 authenticate하라고 srping에게 요청하는 것이다.

## JWT
- JWT는 안전하게 정보를 공유하기 위한 간편한 포맷이다.
- 디지털로 저장되어서 검증하고 신뢰할 수 있다.
- RSA(Rivest-Shamir-Adleman)은 비대칭 암호화 알고리즘이다.
- `<header>.<payload>.<signature>`
  - header: algorithm, token type
  - payload: data
  - signature

#### Creating a Signed JWT
1. { "alg": "RS256 } -> Base64 Encode -> `<header>`
2. { "iss": "self", "sub: "a", "exp": 1234567, "iat": 1234567, "authorities": "ROLE_admin ROLE_user" } -> base64 encode -> <payload>
3. 1과 2를 합쳐서 private key를 이용해 signing alogorithm을 적용하면 `<signature>`가 나온다. 이것을 base64 encoded 한다.

#### 주의사항
- 서명된 토큰은 변조로부터 보호되지만 누구나 읽을 수 있다는 점에 주의해야 한다. 
- secret/sensitive information을 JWT의 payload나 header 요소로 놓으면 안된다.
  - JSON web encryption으로 민감 정보가 암호화되어 있지 않으면 특히 넣으면 안된다.

#### 왜 JWT를 사용하나?
- 서버 측에 세션을 유지할 필요가 없다. 
  - 많은 사용자가 있을 때, RAM을 절약할 수 있다.
- 세션 정보가 필요하지 않으므로 서버가 쉽게 스케일업할 수 있다.
  - JWT는 microservice arhitecture를 위한 좋은 선택이다.
- 토큰에는 사용자의 일부 정보가 들어가 있다.
  - DB 호출을 약간 줄일 수 있다.
- 토큰은 다른 서버들 위에서 authenticate를 위해 사용될 수 있다. (SSO)
- JWT는 오직 토큰 발급만 담당하는 전용 인증 서버를 허용한다.
  - 책임 분리 : 애플리케이션 서버로 요청 처리 vs 사용자 토큰 발급 전담 서버
  - 보안 향상 : 토큰 발급 및 관리가 중앙 집중화 > 일관된 보안 정책
