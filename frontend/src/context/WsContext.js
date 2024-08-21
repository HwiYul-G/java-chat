import { useEffect, createContext, useRef } from "react";
import * as StompJs from '@stomp/stompjs';
import { useUser } from "./UserContext";

const WebSocketContext = createContext();

function WebSocketProvider({children}){
    const {userInfo} = useUser();
    const client = useRef(null);
    const subscriptions = useRef({});
    
    // 하나의 채널을 위해 하나의 콜백을 등록하는 컴포넌트가 호출한다.
    const subscribe = (channel, callback) => {
        if(client.current && client.current.connected && !subscriptions.current[channel]){
            const subscription = client.current.subscribe(channel, message => {
                const data = JSON.parse(message.body);
                callback(data);
            });
            subscriptions.current[channel] = subscription;
        }
    };

    // 콜백을 지운다.
    const unsubscribe = (channel) => {
        if(subscriptions.current[channel]){
            subscriptions.current[channel].unsubscribe();
            delete subscriptions.current[channel];
        }
    };

    const publish = (channel, message) => {
        if(client.current && client.current.connected){
            client.current.publish({
                destination: channel,
                body: JSON.stringify(message),
                headers: {
                    'content-type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            })
        }
    };

    useEffect(() => {
        if(userInfo){ // 사용자가 로그인 상태일 때
            client.current = new StompJs.Client({
                brokerURL: 'ws://localhost:8080/java-chat',
                connectHeaders: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
            });

            client.current.onConnect = () => {
                console.log('STOMP Client connected');
            };

            client.current.onDisconnect = () => {
                console.log('STOMP Client disconnected');
            };

            client.current.activate();
        } else if (client.current) { // 사용자가 로그아웃 상태일 때
            client.current.deactivate();
        }

        return () => {
            if(client.current){
                client.current.deactivate();
            }
        };
    }, [userInfo]); // userInfo가 변경될 때마다 실행

    // WS provider dom
    // subscribe와 unsubscirbe는 context를 위해 유일하게 필요한 prop이다.
    return (
        <WebSocketContext.Provider value={{ subscribe, unsubscribe, publish }}>
            {children}
        </WebSocketContext.Provider>
    );
}

export {WebSocketContext, WebSocketProvider}