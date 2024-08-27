# WebSocet Impl with Stomp
- 목표: Spring Boot로 서버에서 chat service 구현하는 방법 알기(개인, 단체)
- 목차
    1. websocket과 stomp
    2. Spring Boot에서 STOMP 적용하기
    3. Client

## WebSocket과 STOMP
### WebSocket
- 웹소켓 프로토콜은 애플리케이션이 실시간 메시지를 다루는 방법 중 하나이다.
    - long polling, server-sent events같은 대안책도 있다.
- 즉 WebSocket은 Full-Duplex 통신을 가능하게 한다.
- HTTP로 최초의 handshake를 한 후 HTTP connection은 새로 열린 TCP/IP 연결로 업그레이드 한다.
    - 이 업데이트한 새로 열린 TCP/IP 연결을 websocket이 사용한다.
- websocket protocol은 더 낮은 수준의 프로토콜이다.
    - 이 프로토콜은 바이트 스트림이 frame 안으로 transform되는 방식을 정의한다.
    - 하나의 프레임은 하나의 텍스트나 바이너리 메시지를 포함할 수 있다.
    - 이 메시지는 이 자체로 route되는 방법이나 처리하는 방법에 관한 추가적인 정보를 제공하지 않아서 추가적인 코드를 작성해야한다. (복잡해 질 수 있다.)
    - 이런 불편함을 더 쉽게 하는 sub-protocols 중 하나가 STOMP이다.
### STOMP(Simple Text Oriented Messaging Protocol)
- STOMP는 기업용 메시지 브로커에 연결하기 위해서 Ruby, Python, Perl같은 스크립팅 언어로 최초로 만들어진 simple text based messaging protocol이다.
- STOMP는 WebSocket protocol을 over해서 사용한다. (업데이트한 것)
    - WebScoket 프로토콜은 web을 위한 TCP라 불린다.
    - STOMP는 Web을 위한 HTTP라 부른다.
- 웹 소켓 프레임과 매핑되는 프레임 타입(CONNECT, SUBSCRIBE, UNSUBSCRIBE ACK, SEND ...)을 정의할 수 있다.

## Spring Boot에서 STOMP 적용하기
Spring Boot에서 STOMP를 통해서 `pub/sub`을 통해 여러 사용자에게 메시지를 브로드캐스팅할 수도 있고 single user에게 메시지를 보낼 수도 있다.
### 의존성 추가
- `pom.xml`에 websocket 라이브러리 의존성을 추가한다.
- message를 전송하기 위해서 JSON 포맷을 사용한다면 GSON이나 Jackson 의존성을 추가한다.
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>            
  <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```
### WebSocketConfig
#### 코드
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfiurer{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/java-chat")
            .setAllowedOrgins("mydomain.com") 
            .withSockJs();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }
}
```
1. `registerStompEndpoints()`
    - endPoint를 'java-chat'으로 설정하면 `ws://localhost:port번호/java-chat`으로 연결해야 한다.
    - `setAllowedOrigin`
        - websocket과 sockjs는 기본적으로 same origin만 받는다.
        - client를 별도로 하는 경우 그 origin을 등록하면 된다. react인 경우 `http://localhost:3000`이다.
    - `withSockJs()`
        - fallback options으로 웹 소켓이 지원되지 않은 환경에서도 웹소켓이 작동하게 한다.
            - 제한적인 proxies는 http upgrade 수행을 불가능하게 하거나 오랫동안 connection이 open되어 있지 못하게 한다.
            - 이런 경우에 SockJS가 해결한다.
        - SockJS transports는 WebSocket, HTTP Streaming, HTTP Long Polling 순으로 fall 한다.
2. `configureMessageBroker()`
    - simpleBroker에 message를 주고받기 위한 dest를 정한다.
        - 보편적으로 pub-sub모델로 구독한 모든 클라이언트에게 브로드캐스팅하는 것은 topic prefix를 가진다.
        - 개인 채팅 등 private한 것은 queue prefix를 가진다.
    - applciationDestinationPrefixes
        - 컨트롤러에서 구현할 `@MessageMapping`된 메소드를 다루는 목적지를 필터링하기 위해 app prefix를 정의
        - `/app`으로 들어간 컨트롤러는 메시지를 처리한 후 broker에게 message를 보낸다.

#### 동작
<img src='https://docs.spring.io/spring-framework/reference/_images/message-flow-simple-broker.png' width='' height=''/>

1. client가 `/app/a`로 message를 보낸다.
    a. request channel로 들어가서 `SimpAnnotationMethod`로 넘어가서 처리를 하고
    b. broker channel을 통해서 `/topic`으로 넘어가서 `SimpleBroker`로 넘어간다.
    c. response channel로 넘어가서 결론적으로 `/topic/a`로 메시지가 전달된다.
2. client가 `/topic/a`로 message를 보낸다.
    a. request channel로 들어가서 `SimpleBroker`로 바로 넘어가서 처리하고
    b. response channel로 넘어가서 `/topic/a`로 메시지가 전달된다.

### User Requests를 다루는 Controller를 구현한다.
```java
@Controller
public class AController{
    @MessageMapping("/a")
    @SentTo("/topic/a") // topic을 구독하는 모든 사용자에게 return하는 msg를 broadcast한다.
    public String broadcastA(@Payload String message){
        return message;
    }

    @MessageMapping("/b")
    @SendToUser("/queue/b") // 특정한 user에게만 보낸다.
    public String reply(@Payload String message, Principal user){
        // Principal을 통해서 사용자 목적지를 해결한다..
        return "Hello " + message;
    }
}
```
- `@SendTo` 대신에 `SimpleMessagingTemplate`을 사용해서 컨트롤러 안에서 autowire할 수 있다.
```java
@MessageMapping("/a")
public void broadcastNews(@Payload String message) {
  this.simpMessagingTemplate.convertAndSend("/topic/a", message)
}

@MessageMapping("/b")
public void reply(@Payload String message){
    String username = ...;
    this.simpleMessagingTemplate.convertAndSendToUser(username, "/queue/b", "hello " + username);
}
```

## Client
### JavaScript
```javascript
function connect(){
    var socket = new SockJs("/greetings");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // private message를 받기 위해서 /user로 prefix된 /queue/greeting이 필요하다.
        // But the client는 app에 login해야만 Principal object를 server side에서 초기화시킨다.
        stompClient.subscribe('/user/queue/b', (greeting) => {
            showGreeting(JSON.parse(greeting.body).name)
        });
    });
}

function sendName() {
 stompClient.send("/app/greetings", {}, $("#name").val());
}
```

### 참고자료
- https://www.toptal.com/java/stomp-spring-boot-websocket
- https://medium.com/swlh/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
- https://docs.spring.io/spring-framework/reference/web/websocket/stomp/message-flow.html