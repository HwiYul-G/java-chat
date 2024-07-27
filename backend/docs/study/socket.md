웹 소켓 소개
RFC 6455라는 웹 소켓 프로토콜은 하나의 TCP connection위에서 클라이언트와 서버 사이의 full-duplex, two-way 커뮤니케이션을 설립하는 표준적인 방법을 제공한다. RFC 6455라는 웹 소켓 프로토콜은 HTTP와 다른 TCP 프르토콜이다. 하지만 이프로토콜은 HTTP 위에서 80과 443 포트를 사용해서 동작하도록 디자인 되었고 기존의 firewall rules를 재사용하는 것을 허락한다.

웹소켓 상호작용은 HTTP Upgrade header를 사용하는 HTTP 요청으로 시작한다. 이 요청으로 웹 소켓 프로토콜로 전환된다. 
1. Upgrade: webxocket 헤더와 Connection: Upgrade를 보낸다.
2. 200 상태 코드가 아니라 서버는 Switching Protocols를 보낸다.
위와 같은 성공적인 handshake 후에 HTTP upgrade request를 기반으로하는 TCP socket은 클라이언트와 서버에게 둘다 open되어서 message를 send recv 할 수 있게 한다.

웹 소켓이 동작하는 방법에 대한 완벽한 소개는 현 문서의 범위를 넘는다. 이는 RFC6455 HTML5 웹 소켓 챕터를 보거나 웹에 대한 다른 소개나 튜토리얼을 봐라.

웹소켓 버서가 nginx같은 웹 서버 뒤에서 운영되면, 너는 그것을 설정해서 웹 소켓 서버 로 웹 소켓 업그레이드 요청을 보낼 것이다. 마찬가지로 앱이 클라우드 환경에서 운영되면, 웹 소켓 support와 관련된 클라우드 제공자 소개를 확인해라

HTTP vs. WebSocket
웹 소켓이 HTTP와 호환되도록 디자인 되고 HTTP request로 시작됨에도, 2 프로토콜이 매우 다른 아키텍처와 애플리케이션 프로그래밍 모델로 이끄는 것을 이해하는 것은 중요하다.
HTTP와 REST에서 애플리케이션은 많은 URLs로 모델된다. 애플리케이션끼리 상호작용하기 위햇, 클라이언트는 그러한 URLs와 request response style을 접근한다. 서버는 요청을 적절한 핸들러로 라우트 시킨다. 

반면, 웹 소켓은 보통 초기 연결을 위한 URL 한개가 있다. 그 다음에 모든 애플리케이션 메시지는 같은 TCP connection을 따른다. 이것은 완전히 다른 비동기, 이벤트 기반 메시징 아키텍처임을 나타낸다.

웹소켓은 낮은 수준의 전송 프로토콜이다. 즉 HTTP와 달리 메시지의 내용에 어떤 semantics도 처방하지 않는다. 
semantics이 처방되지 않는 것은 클라이언트와 서버가 메시지 semantics에 관한 도으이가 없다면 메시지를 라우트하거나 처리할 어떤 방법도 없다는 것을 의미한다.

웹소켓 클라이어늩와 서버가 더 고수준의 메시지 프로토콜(STOMP같은) 것을 사용하길 협상할 수 있다. 이때는 Sec-WebSocket-Protocol 헤더를 http handshake request에 놓는다. 이 헤더의 부제에도, 그들은 그들 자신의 컨벤션을 사용할 필요가 있다.

웹 소켓을 사용하는 때
웹소켓은 웹 페이지를 다이나믹하고 상호작용하게 한다. 그러나 많은 경우에 ajax와 http streaming의 조합 혹은 긴 polling으로 간단하고 효과적인 해결책을 제공한다.
예를 들어서, 뉴스 메일 소셜 피드는 동적으로 업데이트된다. 그러나 이것은 완전히 매 몇 분마다 okay하지 앟는다. collaboration, games, financial apps같은 경우는 더 실시간이여야한다.

latency alone은 결정 요인이 아니다. 메시지의 양이 상대적으로 낮으면 http streaming이나 polling은 효과적인 해결책을 제공한다.
low latency와 high frequency와 high volume이 웹 소켓에서 적절한 선택이다.

인터넷위에서 너의 통제 권한을 벗어난 제한적인 프록시들은 웹 소켓 상호작용을 못하게 할지도 모른다. 
그들이 업그레이드 헤더 위에서 pass할 설정을 못하기 때문에 혹은 그들이 long lived connection을 닫기 때문일지도 므론다. 이것은 의미한다. firewall 내부에서 인터넷 앱의 웹 소캣 사용은 더 직관적인 결정이다. public facing appplicaiton보다. 




웹소켓 프로토콜은 동시에 양방향 커뮤니케이션에 관한 HTTP 기반의 아키텍처의 한계를 극복하도록 만들어졌다.
가장 중요한 것으로, 웹소켓은 HTTP(request-response)와 또 다른 커뮤니케이션 모델(동시에 양방향 메시징)을 가진다.

웹소켓은 양방향 바이트 스트림 전송을 허용한 TCP 위에서 동작한다.
웹 소켓은 TCP 위에서 thin functionality를 제공한다. 이 TCP는 binary와 text messages를 전송하게 한다. 이 binary와 text messages는 웹의 필수적인 보안 제약을 제공한다.
하지만 웹소켓은 그러한 메시지 포멧을 명시하지 않는다.

웹소켓은 의도적으로 가능한 간단하게 디자인되었다.
추가적인 프로토콜 복잡함을 피하기 위해서, 클라이언트와 서버는 웹 소켓 위의 서브 프로토콜을 사용하도록 의도된다.
STOMP는 웹 소켓 위에서 클라이언트와 intermediate servers(message brokers) 사이의 메시지를 주가 받기 위해서 웹 소켓 위에서 동작하는 그러한 애플리케이션 서브 프로토콜이다.

STOMP(Simple/Streaming TExt Oriented Message Protocol)
STOMP는 클라이언트들이 메시지 브로커를 통해서 서로 메시징하기 위한 interoperable 텍스트 기반 프로토콜이다.
STOMP는 간단한 프로토콜이다. 왜냐하면 STOMP는 메시지 브로커들의 가장 흔하게 사용되는 메시징 동작들의 최소한만 구현하기 때문이다.
STOMP는 streaming protocol이다. 왜냐하면 STOMP는 신뢰할수 있는 양방향 스트리밍 네트워크 프로토콜(TCP, WebSocket, Telnet, 등)위에서 일할 수 있기 때문이다.
STOMP는 텍스트 프로토콜이다. 왜냐하면 클라이언트들과 메시지 브로커는 text frame을 교환하기 때문이다.
이 텍스트 프레임은 의무적으로 command, optional headers, optional body를 포함한다. body는 한 줄의 엔터로 헤더와 분리되어야 한다.

```
COMMAND
header1: value1
header2: value2

body
```
stomp는 messaging protocol이다. 왜냐하면 클라이언트들은 메시지를 생산하고( 브로커 목적지로 메시지를 보내고), 메시지를 소비할 수 있기 때문이다. (프로커의 목적지를 구독하고 그 목적지로부터 메시지를 받는다.)
stomp는 interoperable protocl이다. 왜냐하면 여러 메시지 브로커들(aciveMQ, RabbitMQ, HornetQ, OpenMQ, etc)와 여러 언어와 플랫폼으로 작성된 클라이언트들과 함께 동작할 수 있기 때문이다.

## clients를 broker에 연결하기
### connecting
브로커에 연결하기 위해서 클라이언트는 2가지의 필수 헤더인 accept-version, host를 가진 CONNECT frame을 보내야한다.
- accept-version: client가 지원하는 STOMP protocol 버전
- host: 클라이언트가 연결되길 바라는 가상 호스트의 이름

연결을 허용하기 위해서, 브로커는 클라이언트에게 의무적인 헤더 1개(version)과 함께 CONNECTED frame을 보낸다.
- version: session이 사용하는 STOMP 프로토콜의 버전
### Disconnecting
클라이언트는 소켓을 닫음으로써 언제든 브로커와 연결을 끊을 수 있다. 하지만 그 연결 끊음이 이전에 보낸 프레임들이 브로커에게 도착했음을 보장하지 못한다.
적절하게 연결을 끊기 위해서, 모든 이전에 프레임들을 브로커가 받았음을 보장하기 위해서,
클라이언트는 3가지 단계를 수행해야한다.
1. receipt 헤더를 가진 DISCONNECT 프레임을 보낸다.
2. RECEIPT 프레임을 받는다.
3. 소켓을 닫는다.

## 클라이언트의 메시지를 브로커에게 보내기
목적지로 메시지를 보내기 위해서, 클라이언트는 의무적인 header를 가진 SEND 프레임을 보내야한다.
- destination: 클라이언트가 보내길 원하는 목적지 
만약 SEND frame이 body를 가지면 SEND frame은 반드시 content-length와 content-type headers를 포함해야한다.

## 브로커로부터 메시지들을 구독하기(받기)
한 목적지를 구독하기 위해서, 클라이언트는 2개의 의무적인 헤더(destination, id)를 가진 SUBSCRIBE 프레임을 보내야한다.
- destination: 클라이언트가 구독하길 원하는 목적지
- id: 구독을 위한 고유 식별자

## 메시징
구독된 곳들에서 클라이언트로 메시지를 전송하기 위해서, 서버는 3개의 의무적인 헤더를 가진 MESSAGE frame을 보낸다.
- destination: 메시지가 보내질 목적지
- subscription: 메시지를 받는 구독의 id
- message-id: 그 메시지에 대한 id

## 구독 해제하기
기존의 구독을 제거하기 위해서, 클라이언트는 UNSUBSCRIBE 프레임을 보내야한다.
의무적인 헤더는 아래와 같다.
- id: 구독에 대한 고유 id

## Acknowledgement
프레임을 잃어버리거나 중복되는 것을 피하기 위해서, 하나의 클라이언트와 하나의 브로커가 분산 시스템의 부분이라면, 프레임 ack를 사용하는 것은 필수적이다.
SUBSCRIBE 프레임은 선택적인 ack header를 포함한다. 이 ack header는 메시지의 ack 모드를 통제한다.
ack 모드로는
- auto(기본)
- client
- client-individual

acknowledgement mode가 auto이면 클라이언트는 그것이 받은 메시지를 확인할 필요가 없다.
그 브로커는 메시지가 클라이언트에게 보내지자마자 클라이언트가 받았을 것으로 가정한다.

ack 모드가 client이면, 클라이언트는 반드시 모든 이전의 메시지들에 대해서 서버 confirmation을 보내야만한다.
지정된 메시지뿐 아니라 그 이전에 보내진 메시지들도 ack한다.

ack 모드가 client-individual이면 클라이언트는 특장한 메시지하나만을 위ㅐ서 server confirmation을 보내야만한다.
클라이언트는 ACK 프레임을 사용해서 구독에서 온 메시지의 소비를 확인하기 위해서 ACK 프레임을 사용한다.
클라이언트는 NACK 프레임을 사용해서 구독으로 부터 온 메시지의 소비를 부정할 수 있다.
ACK와 NAK 프레임 둘다 id header를 포함해야만 한다. 그 id 헤더는 ack 되어질 MESSAGE frame의 ack header와 일치해야한다.

## Broker commands acknowledgement
브로커가 receipt를 요청하는 client frame을 성공적으로 처리하면, 클라이언트에게 RECEIPT 프레임을 보낸다.
그 RECEIPT 프레임은 receipt-id header를 포함한다. 이 receipt-id 값은 ack된 명령어의 receipt header와 매칭된다.

스프링 부트로 메시지 보내는 방법
- 서버가 클라이언트에게 한 번 메시지를 보낸다.
- 서버가 클라이언트에게 정기적으로 메시지를 보낸다.
- 서버가 클라이언트로부터 메시지를 받고, 메시지를 기록하고, 다시 클라이언트에게 보낸다.
- 클라이언트가 서버로 비정기적인 메시지를 보낸다.
- 클라이언트가 서버로부터 메시지를 받고 그 메시지를 기록한다.

클라이언트는 /app/subscribe로 SUBSCRIBE 프레임을 보내고
서버는 같은 /app/subscribe 목적지로 MESSAGE 프레임을 보낸다.
brkoer 없이 직접적으로 클라이언트에게 보낸다.

@SubscribeMapping("/subscibe")
public String sendOneTimeMessage(){
    return "server one-time message via the application"
}

@MessageMapping
애플리케이션과 클라이언트 사이의 반복적인 메시지를 위해서 사용
void 타입과 MessageMapping된 메소드는 클라이언트로부터 /app/request를 대사응로 SEND 프레임을 받고
action을 수행하뒤 어떤 response도 보내지 않는다.

아래의 예에서, @MessageMapping과 @SendTo된 메소드는 클라이언트로부터 SEND 프레임을 받고 action 수행 후 /queue/response로 MESSAGE frame을 보낸다.
@MessageMapping("/request")
@SendTo("/queue/responses)
public String handleMessageWithExplicitResponse(String message){
    ...
    return "response to " + HtmlUtils.htmlEscape(message);
}

다음 예시에서, @MEssageMapping 어노테이션되고 String 리턴타입을 가진 함수는 SEND frame을 클라이언트로부터 받고
/app/request로 MESSAGE 프레임을 보낸다.

@MessageExceptionHandler는 @SubscribeMapping과 @MessageMapping 어노테이션된 컨트롤러 안에서 예외 처리를 다루기 위해 사용한다.




### 참고자료
- https://medium.com/swlh/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397