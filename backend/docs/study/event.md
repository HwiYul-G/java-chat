# Spring Events
- 목표: 스프링 부트 Events 이해하고 사용하기
- 학습 내역
    - event가 무엇인지 알고 기본 사용 방법과 다양한 방식의 쓰임 학습
    - 실제로 언제 사용하는지 알기
- 목차
    1. [Events란?](#spring-events)
    2. [기본 Events 사용 방법](#기본-events-사용방법)
        - 클래스 extends이용
        - 어노테이션 이용
    3. [스프링 부트의 내장 events](#spring-boot의-내장-events)
        - ContextRefrshedEvent, ContextStartedEvent, ContextClosedEvent, ApplicationFailedEvent
    4. [여러 방식으로 이벤트 이용하기](#여러-방식으로-event-이용하기)
        - 비동기
        - 조건
        - transactional
        - event execution
        - generic event
        - event listener의 error handling
    5. [event test하는 방법](#event-test)
    6. [Usecase](#usecase)
    7. [잘 사용하는 방법?](#잘-사용하는-방법)

## Events
- Spring boot event는 Spring framework의 context module의 한 부분이다.
- events로 정보를 브로드케스팅해서 강한 결합 없이 상호작용하는 beans을 제공한다.
- event를 통해서 모듈화와 분리를 유지할 수 있다.

### Events를 위한 구성요소
1. event
    - ApplicationEvent class를 extend한 object로 캡슐화 되어 있다.
    - 서로 다른 컴포넌트들 사이의 커뮤니케이션 역할을 한다.(정보 전달을 위함)
2. event publisher
    - event publisher와 application context는 등록된 listeners로 events를 broadcast한다.(전체에게 전달하는 행위)
    - event publisher로 event를 발행하고 이를 듣고 있던 listeners이 일을 하면서 각 컴포넌트들이 직접적인 의존성 없이 상호작용 할 수 있다.
3. application listeners
    - 특정한 events에 반응하는 컴포넌트들로 트리거 되었을 때 커스텀 로직이 실행된다.
    - listeners로 모듈화되고 조건에 따라 반응 하는 등 동적으로 응답하는 디커플링된 시스템을 만들 수 있다.

**즉 event를 event publisher로 발행하면 listeners가 촉발되어서 event에서 받은 정보를 통해서 일을 한다.**

## 기본 Events 사용방법
### 클래스 extends 이용
1. Custom Event를 만든다.
    ```java
    public class CustomEvent extends ApplicationEvent{
        private final String message;
        public CustomEvent(Object source, String message){
            super(source);
            this.message = message;
        }
        public String getMessage(){ return message; }
    }
    ```
2. `ApplicationListener interface`를 구현하고 `onApplicationEvent` 메소드 안에서 수행할 로직을 정의한다.
    ```java
    @Component
    public class CustomEventListener implements ApplicationListener<CustomEvent>{
        @Override
        public void onApplicationEvent(CustomEvent event){
            // custom event를 처리하는 custom logic
            System.out.println("Custom Event Received: " + event.getMessage());
        }
    }
    ```
3. `publishing custom events`
    - `ApplicationEventPublisher interface`는 events를 브로드캐스팅하는 메커니즘을 제공한다.
    - application 안에서 특정 event가 발행되어 listeners에게 동적으로 알리게 된다.
    ```java
    @Service
    public class EventPublisherService{
        @Autowired
        private ApplicationEventPublisher eventPublisher;

        public void publishCustomEvent(String message){
            CustomEvent customEvent = new CustomEvent(this, message);
            eventPublisher.publishEvent(customEvent);
        }
    }
    ```
### 어노테이션 
- annotation으로 event를 더 간단하게 다룰 수 있다.
    - 여러 메소드로 혹은 하나의 메소드로 여러 이벤트를 다룰 수 있어서 더 유연하다.
- `@EventListener` 어노테이션으로 관리되는 bean의 어떤 메소드들은 event listener로 행동한다.
```java
@Component
public class CustomEventListener{ // ApplicationListener<T>를 implement하지 않는다.
    // onApplicationEvent를 override 하지 않는다.
    @EventListener
    public void handleCustomNEvent(CustomEvent event){
        // ... handle event ...
    }
}
```

## Spring Boot의 내장 events
- 앞에서 본 custom event는 애플리케이션의 특정 필요를 지원한다. 
- 반면 애플리케이션 라이프사이클의 여러 측면을 다루는 내장이벤트 레퍼토리를 제공한다.
### ContextRefreshedEvent
- 이 이벤트는 application context가 완전히 refresh될 때 트리거된다.
- 초기화 하는 일을 수행하거나 특정 작업을 트리거하는 순건 제공됨.
### ContextStartedEvent
- application context가 시작될 때 유발된다.
- 이 이벤트는 애플리케이션 라이프사이클을 시작하는 일을 실행할 때 중요하다.
### ContextClosedEvent
- application context가 끝날 때 유발된다.
- application이 shut down 되기 전에 자원을 해제하거나 테스크를 클린업하는 것을 수행할 때 이롭다.
### ApplicationFailedEvent
- application이 시작하기 실패 했을 때 유발된다.
- 이 이벤트는 실패 원인을 제공해서 degradation하거나 fallback 매커니즘을 허락한다.
애플리케이션이 시작하기 실패 했을 때 유발된다. 이 이벤트는 실패의 이유로 통찰력을 제공하고 우아한 degradation이나 fallback 매커니즘을 허락한다.

## 여러 방식으로 event 이용하기
### 비동기 이벤트
- 기본적으로 event listener는 spring boot에서 동기적이다.
    - 모든 event가 event를 처리했을 때 publishEvent method를 block한다.
    - 그러므로 장황한 오퍼레이션에는 적합하지 않다.
- 이벤트 리스너를 비동기적으로 만들기 위해서 2가지 단계를 수행해야한다.
    - 1번: configuration에서 `@EnableAsync`로 비동기 이벤트 리스너를 가능하게 한다.
    - 2번: `@Async` 어노테이션을 리스너에 추가한다.
- 이를 통해서 메소드는 분리된 스레드로 실행된다.
```java
@Configuration
@EnableAsync
public class SpringAsyncConfig{}
```
```java
@Component
public class CustomEventListener {
    @Async
    @EventListener
    public void handleCustomEvent(CustomEvent event) {
        // ... handle event ...
    }
}
```
### 조건적 이벤트
- 특정한 기준에 기반을 두어서 conditional하게 event를 다룰 수 있다.
- `@EventListener`의 `conditon` attribute를 사용해서 이벤트 다룸의 조건을 평가하는 SpEL expression을 정의한다.
```java
@Component
public class CustomEventListener {
    @EventListener(condition = "#event.username.startsWith('admin')")
    public void handleCustomEvent(CustomEvent event) {
        // ... handle event ...
    }
}
```
### Transactional Events
- event가 application transaction management system에 연결되는 것을 허락한다.
- 이는 하나의 transaction의 성공적인 완료 후에만 이벤트가 publish되는 Transactional Events를 수행할 수 있다.
    - listener는 오직 transaction의 성공적인 커밋 이후에만 트리거 된다.
- transaction event 발행을 위해서 `TransactionalApplicationEventPublisher`을 사용한다.
```java
@Service
public class UserRegistrationService {
    @Autowired
    private TransactionalApplicationEventPublisher eventPublisher;

    @Transactional
    public void registerUser(String username) {
        // ... registration logic ...
        eventPublisher.publishEvent(new UserRegistrationEvent(this, username));
    }
}
```
### Event Execution의 순서
- 기본적으로 같은 이벤트를 처리하는 여러 listeners의 실행 순서는 없다.
- 하지만 이 순서가 필요한 시나리오가 있을 수 있다.
- 이를 이ㅜ해서 `Ordered interface`를 구현하거나 `@Order` annotation을 이용한다.
```java
@Component
@Order(1)
public class FirstCustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        // ... handle event ...
    }
}

@Component
@Order(2)
public class SecondCustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        // ... handle event ...
    }
}
```
### Generic Event
- spring boot 4.2에서 등장한 generic event
- event class는 generic event로 추가적인 정보를 포함할 수 있다.
- generic event를 정의하기 위해서 `ResolvableTypeProvider interface`를 사용해야 한다.
```java
public class CustomGenericEvnet<T> extends ApplicationEvent implements ResolvableTypeProvider {
    private T info;

    public CustomGenericEvent(Object source, T info) {
        super(source);
        this.info = info;
    }

    public T getInfo() {
        return info;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(info));
    }
}
```
listeners은 generic event를 기존 event들과 같은 방식으로 listen할 수 있다.
```java
@Component
public class CustomGenericListener {
    @EventListener
    public void handleCustomGenericEvent(CustomGeneric<Info> event) {
        // ... handle event ...
    }
}
```
### Event Listener's Error handling
- 기본적으로 event 발생동안 던져진 예외는 실행을 멈추고 publisher로 전파를 멈춘다.
- 그러나 이 예외를 다루는 다른 메커니즘을 사용할 수도 있다.
    - `@EventListener`의 mode attribute 사용하기
- 혹은 오류만을 다루는 event listener를 만들 수도 있다.

#### `@EventListener`의 mode attribute 사용하기
- 특정 리스너에 대한 error를 핸들링할 수 있다.
- `async mode`: 예외 밠애시 다른 listeners의 실행을 멈추지 않고 publishers에게 전파되지 않는다.
```java
@Component
public class CustomEventListener {
    @EventListener(mode = EventListenerMode.ASYNC)
    public void handleCustomEvent(CustomEvent event) {
        // ... handle event ...
    }
}
```
####  오류만을 다루는 listener 만들기
- 아래의 경우 DataAccessException이 이벤트 처리동안 던져질 때마다 호출된다.
```java
@Component
public class CustomErrorListener {
    @EventListener(condition = "#root.cause instanceof SomeException")
    public void handleCustomError(DataAccessException exception) {
        // ... handle error ...
    }
}
```

## Event Test
- `ApplicationEventPublisher`를 이용해야 한다.
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomEventListenerTest {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CustomEventListener customEventListener;

    @Test
    public void testHandleCustomEvent() {
        // 1. event 생성
        CustomEvent event = new CustomEvent(this, "test msg");
        // 2. event 발행
        eventPublisher.publishEvent(event);

        // ... verify listener behavior ...
    }
}
```
## Usecase
### Async processing
- 비동기적인 일을 통해서 application이 반응적이게 할 수 있다.
- 이는 자원에 처리하는 일이 사용자 경험에 영향 없이 실행되길 고려할 때이다.

### Logging and Audting
- 관련된 이벤트를 구독함으로써 중요한 이벤트를 캡쳐할 수 있다.
- 이는 디테일한 로깅, 분석, complicance 목적을 위한 auditing에 유리하다.

### Notifications
- 특정 이벤트가 발행되고 application 내나 외부 시스템의 여러 부분에 이를 알려야 하는 경우
- EX: 새로운 user가 등록할 때 welcome mail 보낼 때 하나의 listener로 이를 다룬다.

## 잘 사용하는 방법?
1. listeners 안에서 error를 핸들링하자.
2. listener의 로직은 최소화 하자
    - listener는 서로 다른 부분을 연결하는 역할만 한다.
    - substantial logic은 listener를 호출하는 service class 내에서 한다.
3. `order`가 존재하지만 특정한 order로 통제하지 않도록 디자인 한다.
4. conditional events와 transactional event를 신경써서 사용해서 정밀한 통제를 한다.
    - conditional events로 filtering을 할 수 있다.
5. events와 listeners가 추가되고 수정됨에 따라서 이를 최적화 하는 것은 필수적이다.
    - 여러 이벤트를 한 번에 처리하는 event batching같은 기술을 고려해서 개별 event 핸들리으이 오버헤드를 줄인다.
6. event의 과사용은 디버깅을 힘들게 한다.

## 참고자료
- https://medium.com/hprog99/mastering-events-in-spring-boot-a-comprehensive-guide-86348f968fc6
- https://www.baeldung.com/spring-events
- https://naveen-metta.medium.com/mastering-application-events-and-listeners-in-spring-boot-5d3f2fdaf067