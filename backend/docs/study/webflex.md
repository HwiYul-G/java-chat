# WebFlex
- 목표
- 목차
    1. Synchronous VS Asynchronouse Execution과 Blocking과 Non-Blocking Operations
        - Sync vs. Async Execution
        - Blocking vs. Non-Blocking Operation
        - combination
    2. reactive programming
    3. webFlux
        - 컴포넌트
        - 장점
        - crud 예시코드

## Synchronous VS Asynchronouse Execution과 Blocking과 Non-Blocking Operations
### Sync vs. Async Execution
#### Synchronous execution
- task들을 순차적으로 수행하는 것
- 프로세스 a가 계속 진행하기 전에 프로세스 B가 완료되는 것을 기다린다.
- 릴레이 레이스와 같다.
    - a가 b의 바톤을 기다리고 있는 것
#### Asynchronous execution
- task가 완료되길 기다리지 않고 다음 task를 하는 것
- 프로세스 a는 프로세스 b를 초기화하고 b가 끝내는 것을 기다리지 않고 자신의 execution을 계속 한다.
- 한번에 여러 배달들을 준비하는 것과 같다.
    - 각 패키지는 준비되자마자 보내진다.
- 비동기 프로세싱은 멀티테스킹이 가능해서 더 짧은 시간에 효율적으로 더 많은 tasks를 다룬다.
    - 이는 멀티스레딩이나 멀티프로세싱으로 이뤄진다. 

### Blocking vs. Non-Blocking Operation
#### Blocking
- 호출된 함수로 통제권을 전달하는 동작
- caller가 callee가 일을 끝내고 통제권을 줄 때까지 기다린다.
#### Non-Blocking
- 함수가 task를 완수하는 것을 기다리지 않는다.
- caller는 컨트롤(제어권)을 넘기지 않고도 다른 task를 계속 할 수 있다.

###  combination
#### Sync Blocking
- task들은 다른 작업이 진행 중인 동안, 자체 작업을 처리하지 않고(Blocking) 다른 작업의 결과를 순차적으로 처리한다(Sync)
- 한 작업의 결과가 다른 작업에 직접 영향을 미칠 때 유용
#### Async Blocking
- 다른 task가 완료될 때까지 기다린다(Blocking).
- 하지만 결과를 즉시 처리하지 않아 비순차적으로(비동기)로 이어진다.
#### Sync Non-Blocking
- 다른 작업을 기다리지 않고 자체 작업을 처리한다(Non-Blocking)
- 다른 작업의 결과를 순차적으로 즉시 처리한다.(동기)
#### Async Non-Blocking
- 다른 작업을 기다리지 않고 작업을 처리한다(Non-Blocking)
- 결과를 즉시 처리하지 않고 비순차적으로 한다.(비동기)
- 다른 작업의 결과가 자신의 작업에 영향을 미치지 않는 경우에 적합


## reactive programming
- concurrent requests(동시에 발생하는 요청들)을 다룰 때 유리하다.
- reactive programming은 반응적이고 확장적인 파라다임이다.
- 비동기 데이터 스트림을 다루는 이벤트로 유도된다.
- pull 기반 모델이 아니라 push 기반 모델을 사용한다.
    - push기반 모델에서 데이터 생산자는 소비자에게 소비자의 데이터 요청을 기다리지 않고 update를 보낸다.
- spring 환경에선 spring webflex로 reactive applications을 구축한다.
### Reactive Programming의 핵심 개념
1. Reactive Streams
    - non-blocking backPressure로 비동기 스트림 처리를 위한 명세
    - publisher, subscriber, processor로 이루어짐
2. BackPressure
    - backPressure는 들어오는 데이터 속도 > application이 처리하는 속도인 상황을 다루는 매커니즘
    - reactive stream은 이를 consumer가 producer에게 signal을 보내서 data를 느리게 하거나 buffer 처리를 한다.

## webFlux
- mvc 프레임워크의 대안첵을 제공하는 spring framework5에 소개된 web framework
- webflux는 project reactor의 상위에 구축됨
### 컴포넌트
1. Reactive Types: Mono, Flux
    - Mono: 0 ~ 1개의 element를 가진 stream. 하나의 결과를 생성하는 비동기 동작을 위해 사용.
    - Flux: 0 ~ n개의 elements를 가진 stream. 여러 results를 생성하는 비동기 동작을 다루는 데 적합.
2. Handler Functions
    - 들어오는 요청을 다루고 응답을 생성하는 함수
    - Mono와 Flux같은 reactive types에서 동작한다.
3. Router Functions
    - 특정 기준에 따라 핸들러 함수로 요청을 라우터하는 방법 정의

### 장점
1. Reactive Nature
    - 많은 양의 concurrent connection을 다루면서 확장성있고 반응적인 애플리케이션 개발을 하게 함
2. Non-Blocking I/O
    - 더 적은 threads로 많은 요청을 효율적으로 다루기 위해서 non-blocking IO를 사용해 자원을 개선
3. Backpressure Support
    - downstream componets를 압도하는 것 없이 애플리케이션 데이터의 bursts를 다룸
4. Functional Style
    - 불변 데이터 구조로 함수적 스타일을 포용함
    - 현대적이고 선언적인 프로그래밍 파라다임에 적합
### crud 예시 코드
#### 의존성 추가
`spring-boot-starter-webflux` 추가
#### 컨트롤러
```java
@RestController
public class ReactiveUserController{
    @Autowired
    private ReactiveUserService userService;

    @GetMapping("/users")
    public Flux<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Mono<User> getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public Mono<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

}
```
#### 서비스
```java
@Service
public class ReactiveUserService {
    @Autowired
    private UserRepository userRepository;
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }
    public Mono<User> updateUser(String id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    return userRepository.save(existingUser);
                });
    }
    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}
```

## 참고자료
- https://medium.com/@aashigangrade06/learn-reactive-programming-with-spring-webflux-6b53cfd0c038
- https://blog.function12.io/tag/insight/understanding-synchronous-vs-asynchronous-execution-and-blocking-vs-non-blocking-operations/
- https://www.baeldung.com/spring-webflux
- https://docs.spring.io/spring-framework/reference/web/webflux.html