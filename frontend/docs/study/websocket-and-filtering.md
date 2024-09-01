# Websocket and Filtering
- 목표
    - 웹소켓을 이용한 실시간 양방향 통신(full-duplex)
    - 하나의 connectio으로 filtering을 통해 서버의 부하 줄이기
- 목차
    1. [WebSocket이란?](#websocket이란)
        - [HTTP 프로토콜의 아쉬운 점](#http-프로토콜의-아쉬운-점)
        - [WebSocket](#websocket)
    2. [`useCallback` Hook 보고가기](#usecallback-hook-보고가기)
    3. [WebSocket Protocol과 SubProtocl](#websocket과-subprotocol)
    4. [context-api를 통해 connection을 하나로 하기](#context-api를-통해-connection을-하나로-하기)

## WebSocket이란?
### HTTP 프로토콜의 아쉬운 점
- http는 한 방향 커뮤니케이션 패턴을 가지며 request-response를 따른다. 
- http의 `polling`, `streaming`, `server-sent events(SSE)`와 같은 다양한 메소드로 데이터의 전송을 구현할 수 있다. 
- 하지만, 즉각적인 업데이트를 하는 것, full duplex엔 부족했다. 
- 이에 websocket 프로토콜로 실시간 커뮤니케이션 시스템을 구현하게 되었다. 
#### Polling
- 서버 간격을 polling한다.
- client는 `setInterval`, `setTimeout`을 사용해 미리 정의된 간격으로 서버에 요청을 보낸다.
- long polling에서는 서버가 간격이나 대기 시간을 처리한다.
- http polling lifecycle = http protocol의 요청 + http protocol response event
##### HTTP Polling의 단계
1. 요청 전송: 클라이언트는 서버와 통신을 위해 주기적으로 요청을 보낸다.
2. 응답 대기: 서버는 업데이트나 변화가 발생할 때까지 요청을 대기상태로 유지하거나 일정시간 응답을 보류한다.
3. 응답 전송: 서버는 업데이트나 변화가 생기면 response를 보낸다.
4. 사이클 반복: 위 사이클은 클라이언트가 새로운 request를 보낼때까지 지속된다.

#### HTTP Streaming
- HTTP Streaming은 HTTP Polling을 개선하려고 생겼다.
- HTTP Polling에서 클라이언트에 응답을 보내지 않고 서버가 닫힌다.
    - 이는 http polling과 관련된 네트워크 대기 시간이 주 원인이다.
    - http polling에선 서버가 응답 후 요청 연결 채널을 닫는다.
    - 연결 채널을 닫는 것은 클라이언트가 새 요청이 있을 때마다 새 연결을 생성해야 함을 의미한다.
- http stream에선 서버가 클라이언트의 요청에 데이터로 응답한 후 초기 요청이 열려있다.
    - 요청 채널을 무기한 열어두면 새 데이터를 사용할 수 있거나 업데이트나 변경이 있을 때마다 서버가 클라이언트에 계속 응답을 보낼 수 있다. 
- http stream은 대기 시간을 줄이고(연결을 또 하지 않으므로), 거의 실시간으로 업데이트를 제공해서 서버 리소스를 활용한다.
- (단점) 하지만, http를 통한 streaming data와 관련된 제한 사항은 클라이언트가 요청을 시작하고 연결을 설정해야 하는 것 + 여전히 대기 시간이 스트림에서도 문제가 될 수 있다.

#### Server Side Event(SSE)
- SSE는 서버에서 클라이언트로 **단방향** 커뮤니케이션 채널을 제공해서 실시간에 가까운 데이터를 스트리밍한다.
- SSE는 표준화된 HTTP 스트리밍 프로토콜로 내장 브라우저 API가 있다.
    - 파이어폭스는 service workers에서 SSE 사용을 지원하지 않는다.
- UseCase
    - 소셜 미디어의 피드 업데이트를 알림
    - 실시간 dashboard를 다룰 때 유용

### WebSocket
- 웹소켓은 양방향, 실시간, full duplex 데이터 전송 프로토콜이다.
- single, long-lived TCP(Transmission Control Protocol) connection을 over한 서버와 클라이언트 사이의 커뮤니케이션이다.
- 클라이언트는 websockeet api로 requests를 보내고 서버에서 event-driven response를 받는다.
#### WebSocket Connection 순서
웹 소켓 연결은 다음의 순서를 따른다.
1. Request Handshake
    - connection을 설립하기 위해서 클라이언트는 서버로 초기 데이터를 보낸다.
    - 이것이 websocket handshake이다.
2. Validate Request
    - request를 받고 서버는 request를 validate한다.
    - validation이 성공이면 연결한다.
3. Communication
    - validation 성공 후 웹소켓 연결이 완전히 설립되었다.
    - 서버와 클라이언트는 서로 데이터를 주고 받는다.

## `useCallback` Hook 보고가기
- `useCallback` Hook은 기억된 콜백 함수이다.
    - 기억(meomoization)은 값을 캐싱해서 다시 계산할 필요 없게 하는 것이다.
- `useCallback`은 자원을 고립시키기 위한 강력한 함수로 사용된다.
    - 그 결과 `useCallback`은 매 렌더링마다 자동적으로 실행되지 않는다.
    - `useCallback` 훅은 자신의 dependencies가 업데이트될 때만 실행된다.
    - 이를 통해 성능을 개선할 수 있ㅏ.
- 참고
    - useCallback과 useMemo 훅은 유사하다.
    - 주요한 차이점은 useMemo는 기록된 값을 리턴하고 useCallback은 기록된 함수를 리턴한다.

## WebSocket과 SubProtocol
- 웹소켓은 통신 매체이다.
- sub protocol은 실제로 websocket protocol의 확장이다.
    - 특정 형식을 설명하거나
    - websocket 연결 내부의 메시징 구조를 더 자세히 설명한다.
- 하위프로토콜은 영어나 스페인어와 같이 두 당사자 간에 사용하기로 합의된 언어로 볼 수 있다.

### SubProtocol의 종류
- STOMP(Simple Text Oriented Messaging Protocol)
    - 주로 메시징 앱에서 적용된다.
    - 쉽게 이해하고 빠르게 구현할 수 있는 메시지에 대한 간단한 텍스트 기반 프로토콜 제공
- MQTT(Message Queuing Telemetry Transport)
    - 낮은 대역폭, 높은 대기시간 네트워크에 적용됨
    - 경량 메시지 프로토콜
    - IoT에 주로 사용
- WAMP(Web Application Messaging Protocol)
    - RPC(Remote Procedure Call)와 Pub/Sub 통신 패턴을 라우팅 방식으로 지원
    - 구조화된 메시징 패턴 사용에 적합
- custom할 수 도 있다.

#### Client
```javascript
// connection 동안 바라는 subprotocol 명세
const ws = new WebSocket("wss://example.com/socket", ["mqtt"]);

ws.open = () => {
    console.log("MQTT 서브 프로토콜로 웹소켓 연결 설립");
};

ws.onmessage = (message) => {
    // MQTT 프로토콜에 따라서 들어오는 메시지를 다룬다.
    console.log("Received message:", message.data);
};
```
#### Server
```javascript
const WebSocket = require("ws");

const wss = new WebSocket.Server({ port: 8080 });

wss.on("connectin", (ws, req) => {
    const subprotocol = ws.protocol;
    console.log(`Client connected using subprotocol: ${subprotocol}`);

    ws.on("message", (message) => {
        // Process message according to the selected subprotocol
        console.log("Received:", message);
    });

    ws.send("Welcome to the WebSocket server!");
});
```

## context-api를 통해 connection을 하나로 하기
- application이 하나의 websocket connection을 글로벌하게 공유해야 한다.
- react context api는 websocket state를 유지할 최적의 장소이다.
### The Provider
- websocket 초기화
- 초기화한 websocket을 createContext로 감싼다.
- 최종적으로 Context Provider를 단순한 컴포넌트로 만든다.
```javascript
import { useEffect, useState, createContext, ReactChild } from "react";

const ws = new WebSocket("소켓 url");

export const SocketContext = createContext(ws);

interface ISocketProvider{
    children: ReactChild;
}

export const SocketProvider = (props: ISocetProvider) => (
    <SocketContext.Provider value={ws}>{props.children}</SocketContext.Provider>
);
```
- 이제 socketProvider는 App의 root 근처에서 오직 한 번 사용될 수 있다.
```javascript
const MyApp = () => {
  return <SocketProvider>{/* Children... */}</SocketProvider>;
};
```
### The Hook
- context와 provider를 가지고 있으므로 custom hook 안에서 이를 사용할 수 있다.
- 현재의 websocket에서 pull하고 웹 소켓을 리턴할 것.
```javascript
import { SocketContext } from "./SocketProvider";
import { useContext } from "react";

export const useSocket = () => {
    const socket = useContext(SocketContext);
    return socket;
};
```
### Events 방출시키기
- useSocket hook을 사용해서 socket을 외부로 노출시킨다.
    - 컴포넌트 안에서 socket을 사용할 수 있다.
- socket에서 send method를 호출한다.
    - 이때 `JSON.stringify()` 안에서 인자를 감싸면서 메시지를 서버로 string으로 전달한다.
```javascript
import { useSocket } from "./useSocket";
cosnt MyComponent = () => {
    const socket = useSocket();

    return (
        <button onClick={() => {
            socket.send(
                JSON.stringify({hello: "World", })
            );
        }}/>
    );
};
```
### 이벤트를 listening하기
- `async` 특성 때문에 events를 리스닝하는 것은 이벤트를 방출시키는 것(send)보다 event를 listening을 더 복잡하다.
- websocket에서 오는 messages위에 event listener를 필수적으로 추가해야 한다.
    - 이때 topic을 벗어난 것은 **필터링** 해야 하고
    - 관련 있는 것만 반응해야 한다.
- 이 상황은 `useCallback` hook의 좋은 usecase이다.
    - component를 마운트하거나 언마운트함에 따라 준비하고 없애고 하는 함수를 원하므로
    - useCallback hook을 사용해서 어던 array로 전달되고 final argument인 어떤 properties가 변화될 때 함수가 유일하게 초기화됨을 보장할 수 있다.
```javascript
import { useCallback, useEffect } from "react";
import { useSocket } from "./useSocket";

const SomeOtherComponent = () => {
    const socket = useSocket();

    const onMessage = useCallback((message) => {
        const data = JSON.parse(message?.data);
        // ...
    }, []);

    useEffect(() => {
        socket.addEventListener("message", onMessage);

        return () => {
            socket.removeEventListener("message", onMessage);
        };
    }, [socket, onMessage]);

    return (
        <button onClick={() => {
            socket.send(
                JSON.stringify({hello: "World", })
            );
        }}/>
    );
};
```

#### Disconnection
- 웹소켓은 언제든지 drop off할 수 있다.
- connection이 무한히 지속된다는 어떤 보장도 없어서 connection이 drop되면 재연결을 할 수 있다.
- SocketProvider에서 useState 안에 websocket instance를 wrap해보자.
- 그 방식으로 소켓이 disconnect되었을 때 우리는 이벤트를 듣고 새로운 웹 소켓 연결을 만들어서 대체할 수 있다.
```javascript
import { useEffect, useState, createContext, ReactChild } from "react";

const webSocket = new WebSocket("MY_SOCKET_URL");

export const SocketContext = createContext(webSocket);

interface ISocketProvider {
  children: React.ReactChild;
}

export const SocketProvider = (props: ISocketProvider) => {
  const [ws, setWs] = useState < WebSocket > webSocket;

  useEffect(() => {
    const onClose = () => {
      setTimeout(() => {
        setWs(new WebSocket(SOCKET_URL));
      }, SOCKET_RECONNECTION_TIMEOUT);
    };

    ws.addEventListener("close", onClose);

    return () => {
      ws.removeEventListener("close", onClose);
    };
  }, [ws, setWs]);

  return (
    <SocketContext.Provider value={ws}>{props.children}</SocketContext.Provider>
  );
};
```

## 참고자료
- https://www.w3schools.com/react/react_usecallback.asp
- https://refine.dev/blog/react-websocket-tutorial-nodejs/#why-websocket
- https://refine.dev/blog/react-websocket-tutorial-nodejs/#introduction
- https://dev.to/raunakgurud09/websockets-unlocked-mastering-the-art-of-real-time-communication-2lnj