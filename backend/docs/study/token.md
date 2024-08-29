# Token
- 목표: Spring Boot Token Authentication 구현하기
- 목차
    1. [용어 정리](#용어-정리)
    2. [JWT란? - 그 구성에 관해서](#jwt-구성요소)
    3. [방법1 - Spring Boot에서 Token Based Authentication 구현](#방법1---spring-boot에서-token-based-authentication-구현)
    4. [방법2 - Spring Boot에서 Token Based Authentication 구현](#방법2---spring-boot에서-token-based-authentication-구현)
    5. [Authentication Error Status Code](#authentication-error-status-code)

## 용어 정리
#### Authentication
- 제공된 credentials로 user의 identity를 검증하는 과정
- Who are you?
#### Authorization
- user가 특정한 동작에 권한(허가)가 있는 지 확인하는 과정
- Can you do this?
#### Principle
- 현재 authenticate된 user
#### Granted authority
- authenticate된 user의 permission(권한, 허가)
#### Role
- authenticated user의 허가 그룹
- Admin, User(일반 사용자)로 나누어서 granted authorty를 처리함

## JWT란?
- JSON Web Token은 웹에서 정보를 안전하게 전달하기 위해 사용되는 JSON Object이다.
- 주로 authentication system을 위해서 혹은 정보 교환을 위해 사용된다.
### JWT 구성요소
```
[header].[payload].[signature]
```
#### header
- header는 token에 사용된 암호화 복호화 기술이 무엇인지 설명한다.
```JSON
{
    "typ": "JWT",
    "alg": "HS256"
}
```
#### payload
- 실제로 사용자 정보가 추가되어있는 부분이다.
- 이 데이터는 JWT의 `claims(권한)`라고 불린다.
    - header와 달리 여러 정보를 넣을 수 있다.
    - 이 정보는 누구든 읽을 수 있으므로 중요한 정보는 넣으면 안된다.
```JSON
{
    "userId": "aaaaaa",
    "iss": "https://example.com/", // 누가 발행했는지 issure
    "sub": "auth/some-hash-here",  // subject(주제)
    "exp": 153452683    // expiration date 만기일
}
```
#### signature
- token의 authenticity(진짜임)를 검증하는 데 사용한다.
```
HASHINGALGO(
    base64UrlEncode(header) + "." + base64UrlEncode(payload), 
    secret
) 
```

## 방법1 - Spring Boot에서 Token Based Authentication 구현
1. `UserDetails`를 구현한 `User` entity
    - `getAuthorities()` 메소드는 user role list를 리턴한다.
        - 이 role list는 permission을 관리하는 데 사용한다.
        - role 기반 접근 control을 할 필요 없는 경우 지금처럼 empty list를 리턴하기도 한다.
    - `getUsername()` 메소드는 사용자에 관한 unique information인 email address를 리턴한다.
    - `isAccountNotExpired()`, `isAccountNonLocked()`, `isCredentialsNonExpired()`, `isEnabled()`가 true이다. 그렇지 않으면 authentication 이 fail이 된다. 
    - 이 메소드들은 필요에 따라 로직을 커스텀할 수 있다.
    <details>
        <summary> User Entity Code</summary>
    
        ```java
        @Table(name = "users")
        @Entity
        @Getter @Setter
        public class User implements UserDetails {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            @Column(nullable = false)
            private Integer id;

            @Column(nullable = false)
            private String fullName;

            @Column(unique = true, length = 100, nullable = false)
            private String email;

            @Column(nullable = false)
            private String password;

            @CreationTimestamp
            @Column(updatable = false, name = "created_at")
            private Date createdAt;

            @UpdateTimestamp
            @Column(name = "updated_at")
            private Date updatedAt;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getUsername() {
                return email;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        }
        ```
    </details>
2. UserRepository 구현
    <details>
        <summary> UserRepsitory Code </summary>

        ```java
        @Repository
        public interface UserRepository extends CrudRepository<User, Long>{
            Optional<User> findByEmail(String email);
        }
        ```
    </details>
3. JWT service 만들기
    <details>
        <summary>Application Properties에 expir time과 secret Key 등록</summary>
        - [JWT secretkey 생성 사이트](https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com)
        - secret key는 항상 256bit의 HMAC hash string이어야 한다. 

        ```application.properties
        security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
        # 1h in millisecond
        security.jwt.expiration-time=3600000
        ```
    </details>
    <details>
        <summary>JWT service code</summary>
        - 토큰 생성, 토큰 타당성 검사, 토큰 Claims 추출 등의 일을 함.

        ```java
        @Service
        public class JwtService {
            @Value("${security.jwt.secret-key}")
            private String secretKey;

            @Value("${security.jwt.expiration-time}")
            private long jwtExpiration;

            public String extractUsername(String token) {
                return extractClaim(token, Claims::getSubject);
            }

            public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                final Claims claims = extractAllClaims(token);
                return claimsResolver.apply(claims);
            }

            public String generateToken(UserDetails userDetails) {
                return generateToken(new HashMap<>(), userDetails);
            }

            public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
                return buildToken(extraClaims, userDetails, jwtExpiration);
            }

            public long getExpirationTime() {
                return jwtExpiration;
            }

            private String buildToken(
                    Map<String, Object> extraClaims,
                    UserDetails userDetails,
                    long expiration
            ) {
                return Jwts
                        .builder()
                        .setClaims(extraClaims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                        .compact();
            }

            public boolean isTokenValid(String token, UserDetails userDetails) {
                final String username = extractUsername(token);
                return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
            }

            private boolean isTokenExpired(String token) {
                return extractExpiration(token).before(new Date());
            }

            private Date extractExpiration(String token) {
                return extractClaim(token, Claims::getExpiration);
            }

            private Claims extractAllClaims(String token) {
                return Jwts
                        .parserBuilder()
                        .setSigningKey(getSignInKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            }

            private Key getSignInKey() {
                byte[] keyBytes = Decoders.BASE64.decode(secretKey);
                return Keys.hmacShaKeyFor(keyBytes);
            }
        }
        ```
    </details>
4. AppConfig
    - db에 사용자를 찾아서 authentication을 수행하고, 이것이 성공했을 때 JWT 생성을 위함
    <details>
        <summary>AppConfig Code</summary>
    
        ```java
        @Configuration
        public class ApplicationConfiguration{
            private final UserRepository userRepository;

            // user repo를 사용해서 user를 가져오는 방법 정의
            @Bean
            UserDetailsService userDetailsService(){
                return username -> userRepository.findByEmail(usrename)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }

            @Bean
            BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }

            @Bean
            public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
            }

            // authentication을 위한 새로운 전략을 셋팅한다.
            @Bean
            AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

                authProvider.setUserDetailsService(userDetailsService());
                authProvider.setPasswordEncoder(passwordEncoder());

                return authProvider;
            }

        }
        ```
    </details>
5. Authentication Middleware 생성하기 - JwtAuthenticationFilter
    - 모든 요청에 대해서 `Authorization` header에서 JWT를 가져오고 이를 검증하길 원한다.
        - token이 validate하지 않으면, 이때 검증해서 요청을 거절한다.
        - token이 validate하면
            1) token에서 username을 추출하고 
            2) DB에서 이를 찾고
            3) authentication context를 셋팅해서 application layer에서 접근할 수 있게 한다.
    <details>
        <summary>JwtAuthenticationFilter</summary>

        ```java
            @Component
            public class JwtAuthenticationFilter extends OncePerRequestFilter {
                private final HandlerExceptionResolver handlerExceptionResolver;
                private final JwtService jwtService;
                private final UserDetailsService userDetailsService;

                @Override
                protected void doFilterInternal(
                    @NonNull HttpServletRequest request,
                    @NonNull HttpServletResponse response,
                    @NonNull FilterChain filterChain
                ) throws ServletException, IOException {
                    final String authHeader = request.getHeader("Authorization");

                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    try {
                        final String jwt = authHeader.substring(7);
                        final String userEmail = jwtService.extractUsername(jwt);

                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                        if (userEmail != null && authentication == null) {
                            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                            if (jwtService.isTokenValid(jwt, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                            }
                        }

                        filterChain.doFilter(request, response);
                    } catch (Exception exception) {
                        // try-catch로 로직을 감싸서 HandleExceptionResolver로 error를 global exception handler로 전달하며 exception을 forwarding한다.
                        handlerExceptionResolver.resolveException(request, response, null, exception);
                    }
                }
            }
        ```
    </details>
    - 추가 학습
        - email address로 사용자를 찾을 때 caching을 통해 수행을 개선할 수 있다. 
        - [관련 캐싱 자료 인터넷 검색](https://blog.tericcabrel.com/data-caching-spring-boot-redis/)
6. SecurityConfig - application requester filter에 설정하기
    - application middle ware로 전달하기 전에 들어오는 request를 매칭하는 기준을 정의해야한다.
        - 새로운 사용자를 등록하거나 로그인하는 URL에는 authentication이 필요하지 않고
        - 그 외에는 필요하다.
        - POST와 GET에는 CORS 설정을 하는 등의 처리를 해야한다.
    <details>
        <summary></summary>
        - `JwtAuthenticationFilter`를 SecurityConfig에 추가해야한다.

        ```java
        @Configuration
        @EnableWebSecurity
        public class SecurityConfiguration {
            private final AuthenticationProvider authenticationProvider;
            private final JwtAuthenticationFilter jwtAuthenticationFilter;

            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf().disable()
                        .authorizeHttpRequests()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
            }

            @Bean
            CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of("http://localhost:8005"));
                configuration.setAllowedMethods(List.of("GET","POST"));
                configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**",configuration);

                return source;
            }
        }
        ```
    </details>
7. authentication service 만들기
    - 새로운 user를 등록하거나 기존 사용자를 authenticationg(로그인)할 때 사용한다.
    - 이를 위해서 필요한 DTO를 만든다.
        - 이때 validate하는 방법을 정리해둔 [인터넷 자료](https://medium.com/@tericcabrel/validate-request-body-and-parameter-in-spring-boot-53ca77f97fe9)
    <details>
        <summary>AuthService Code</summmary>
    
        ```java
        @Service
        public class AuthenticationService {
            private final UserRepository userRepository;
            private final PasswordEncoder passwordEncoder;
            private final AuthenticationManager authenticationManager;

            public User signup(RegisterUserDto input) {
                User user = new User()
                        .setFullName(input.getFullName())
                        .setEmail(input.getEmail())
                        .setPassword(passwordEncoder.encode(input.getPassword()));
                return userRepository.save(user);
            }

            public User authenticate(LoginUserDto input) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                input.getEmail(),
                                input.getPassword()
                        )
                );
                return userRepository.findByEmail(input.getEmail())
                        .orElseThrow();
            }
        }
        ```
    </details>
8. login과 register를 담당하는 AuthController와 token을 받아서 인증하는 UserController를 작성한다.
9. 추가 global exception handler
    <detalis>
        <summary>Global Exception Handler Code</summary>

        ```java
        @RestControllerAdvice
        public class GlobalExceptionHandler {
            @ExceptionHandler(Exception.class)
            public ProblemDetail handleSecurityException(Exception exception) {
                ProblemDetail errorDetail = null;

                // TODO send this stack trace to an observability tool
                exception.printStackTrace();

                if (exception instanceof BadCredentialsException) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
                    errorDetail.setProperty("description", "The username or password is incorrect");

                    return errorDetail;
                }

                if (exception instanceof AccountStatusException) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                    errorDetail.setProperty("description", "The account is locked");
                }

                if (exception instanceof AccessDeniedException) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                    errorDetail.setProperty("description", "You are not authorized to access this resource");
                }

                if (exception instanceof SignatureException) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                    errorDetail.setProperty("description", "The JWT signature is invalid");
                }

                if (exception instanceof ExpiredJwtException) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                    errorDetail.setProperty("description", "The JWT token has expired");
                }

                if (errorDetail == null) {
                    errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
                    errorDetail.setProperty("description", "Unknown internal server error.");
                }

                return errorDetail;
            }
        }
        ```
    </details>

## 방법2 - Spring Boot에서 Token Based Authentication 구현
1. User Entity를 구현한다.
    - 보통 여기에서 `UserDetails`를 implements해서 authentication을 하려고 한다.
    - 경험상 이렇게 하면 코드가 길어지는 불편함으로 User를 삽입해서 만든 별도의 `MyUserPrincipal`을 만들어서 `UserDetails`를 구현한다.
    <details>
        <summary> User Entity Code </summary>

        ```java
            import jakarta.persistence.*;
            import org.hibernate.annotations.CreationTimestamp;
            import org.hibernate.annotations.UpdateTimestamp;

            import java.util.Date;

            @Table(name = "users")
            @Entity
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @Getter
            @Setter
            public class User {
                @Id
                @GeneratedValue(strategy = GenerationType.AUTO)
                @Column(nullable = false)
                private Integer id;

                @Column(nullable = false)
                private String fullName;

                @Column(unique = true, length = 100, nullable = false)
                private String email;

                @Column(nullable = false)
                private String password;

                @Column
                private boolean enabled;

                @Column(nullable = false)
                private String roles

                @CreationTimestamp
                @Column(updatable = false, name = "created_at")
                private Date createdAt;

                @UpdateTimestamp
                @Column(name = "updated_at")
                private Date updatedAt;
            }
        ```
    </details>

2. `UserDetails`를 구현한 `MyUserPrincipal` 클래스를 만든다.
    - 이때 `getUsername()`메소드를 override할 때 주의를 기울인다.
    <details>
        <summary> MyUserPrincipal Code </summary>

        ```java
        @RequiredArgsConstructor
        public class MyUserPrincipal implements UserDetails{

            private final User user;

            // 이 부분은 role기반으로 authority를 확인하는 경우에 사용
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities(){
                // 1. 별 다른 것을 사용하지 않는 경우
                // return List.of();
                // 2. 여러 역할이 있는 경우 " "로 구분한다. 각 권한에 대해 `ROLE_역할이름`으로 처리해서 list를 준다.
                return Arrays.stream(StringUtils.tokenizeToStringArray(user.getRoles(), " "))
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();
            }

            @Override
            public String getPassword(){
                return user.getPassword();
            }

            @Override
            public String getUsername(){
                // 주의! 이 부분에 원하는 username을 설정한다.
                // 현재 username은 김OO처럼 중복될 수 있으므로 email을 처리하게 함.
                // username이라기엔 id에 가깝다고 생각하고 처리할 것
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.isEnabled();
            }

            public User getUser() {
                return user;
            }
        }
        ```
    </details>
3. `UserRepository`에서 `getUsername()`처리할 것을 가져오는 메서드를 추가한다.
    <details>
        <summary> userRepository Code </summary>

        ```java
        @Repository
        public interface UserRepository extends JpaRepository<User, Long>{
            // getUsername()에서 처리할 것
            Optional<User> findByEmail(String email);
        }
        ```
    </details>
4. `UserDetailsService`를 구현한 서비스 클래스가 필요하다.
    - 여기서 `loadUserByUsername` 메소드를 오버라이드해야한다.
    <details>
        <summary> userService에서 다루기</summary>

        ```java
        @Service
        public class UserService implements userDetailsService{
            private final UserRepository userRepository;
            // -  새로운 사용자를 register할 때 비밀번호를 인코딩
            // - config에서 bean으로 등록해 놓고 사용
            private final PasswordEncoder passwordEncoder;
            ...
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
                return userRepository.findByEmail(email)
                    .map(MyUserPrincipal::new) // 여기서 MyUserPrincipal로 mapping해주는 것이 중요
                    .orElseThrow(() -> new UsernameNotFoundException("userEmail: "+ useEmail + "을 찾을 수 없습니다."));
            }
        }
        ```
    </details>
5. SecurityConfig
    <details>
        <summary> SecurityConfig Code</summary>
    
        ```java
        @Configuration
        public class SecurityConfig{
            private final RSAPublicKey publicKey;
            private final RSAPrivateKey privateKey;

            @Value("${api.endpoint.base-url}")
            private String baseUrl;

            public SecurityConfig(){

                // public/private key pair를 생성한다.
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048); // The generated key will have a size of 2048 bits
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                this.publicKey = (RSAPublicKey) keyPair.getPublic();
                this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            }

            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
                // 필요에 맞게 필터 처리할 것.
                return http
                    .authorizeHttpReqeust(requests -> ... 매칭 처리)
                    .headers()
                    .csrf()
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .httpBasic(...)
                    ...
                    .build();
            }

            @Bean
            public CorsConfigurationSource corsConfigurationSource(){
                // cors할 것을 정리한다.
            }

            @Bean
            public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
            }

            @Bean
            public JwtEncoder jwtEncoder() {
                JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
                JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
                return new NimbusJwtEncoder(jwkSet);
            }

            @Bean
            public JwtDecoder jwtDecoder() {
                return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
            }

            @Bean
            public JwtAuthenticationConverter jwtAuthenticationConverter() {
                // jwt로 사용자 권한 관리시, 토큰에 포함된 claims(권한)을 변환해 spring-security의 GrantedAuthority 객체로 변환
                JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

                jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // jwt에서 claim 찾을 때 사용할 이름 지정
                jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

                return jwtAuthenticationConverter;
            }
        }
        ```
    </details>
6. JWTProvider
    - JWT Repository처럼 역할한다고 생각하자. 이는 Service에 주입해서 사용한다.
    - JWT를 create하는 것만 있지만 여기서 validate하는 것도 있으면 좋을 것 같다.
    <details>
        <summary> JWT Provider Code </summary>
    
        ```java
        @Component
        @RequiredArgsConstructor
        public class JWTProvider {

            private final JwtEncoder jwtEncoder;

            public String createToken(Authentication authentication) {
                Instant now = Instant.now();
                long expiresIn = 2; // 2 hours

                // Prepare a claim called authorities
                String authorities = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" ")); // MUST BE space-delimited

                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim("authorities", authorities)
                        .build();

                return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            }
        }
        ```
    </details>
7. AuthService
    - JWTProvider를 주입받아서 사용한다.
    <details>
        <summary></summary>

        ```java
        @Service
        @RequiredArgsConstructor
        public class AuthService {

            private final JWTProvider jwtProvider;
            private final UserToUserDtoConverter userToUserDtoConverter;

            public Map<String, Object> createLoginInfo(Authentication authentication) {
                // Create user info.
                MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
                UserDto userDto = userToUserDtoConverter.convert(principal.getUser());

                // Create a JWT.
                String token = jwtProvider.createToken(authentication);

                Map<String, Object> loginResultMap = new HashMap<>();

                loginResultMap.put("userInfo", userDto);
                loginResultMap.put("token", token);

                return loginResultMap;
            }
        }
        ```
    </details>
8. AuthenticationEntryPoint, AccessDeniedHandler, AuthenticationEntryPoint을 이용하거나 이를 구현한 클래스를 만들어서 filterChain에 추가한다.

## Authentication Error Status Code
- Bad Login credentials: 401
- Account locked: 403
- Not authorized to access a resource: 403
- Invalid JWT: 401
- JWT has expired: 401 status code

## 참고자료
- https://medium.com/@sallu-salman/implementing-token-based-authentication-in-a-spring-boot-project-dba7811ffcee
- https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
- https://www.toptal.com/spring/spring-security-tutorial
- https://www.geeksforgeeks.org/json-web-token-jwt/?ref=gcse