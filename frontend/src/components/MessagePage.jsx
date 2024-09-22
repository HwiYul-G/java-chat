import React, {useContext, useEffect, useState} from 'react'
import {useLocation, useParams} from 'react-router-dom'
import ChatMessage from './ChatMessage';
import './css/_messagePage.css';
import {useUser} from '../context/UserContext';
import {WebSocketContext} from '../context/WsContext';
import {getMessagesByRoomId} from "../api/chatApi";

const MessagePage = () => {
    const {userInfo} = useUser();
    const params = useParams();  // console.log("params : ", params.roomId);
    const {subscribe, unsubscribe, publish} = useContext(WebSocketContext);

    const [allMessages, setAllMessages] = useState([]);
    const [message, setMessage] = useState({
        senderId: userInfo.id,
        roomId: params.roomId,
        senderName: userInfo.username,
        content: '',
        type: 'CHAT',
    });

    const [isDetectingBadWord, setIsDetectingBadWord] = useState(false);

    const location = useLocation();
    const {groupChatRoomName, isGroup, friendName, friendEmail} = location.state || {};

    const handleOnChange = (e) => {
        const {name, value} = e.target;
        setMessage(prev => ({...prev, [name]: value}));
    };

    const handleSendMessage = () => {
        if (message.content === '')
            return;
        publish(`/pub/chat-rooms/${params.roomId}`, message);
        setMessage({
            senderId: userInfo.id,
            roomId: params.roomId,
            senderName: userInfo.username,
            content: '',
            type: 'CHAT',
        });
    };

    const loadMessages = async () => {
        try {
            const res = await getMessagesByRoomId(params.roomId);
            if (res.data && res.data.length > 0) {
                setAllMessages(res.data);
            }
        } catch (err) {
            console.error("Error loading messages: ", err);
        }
    };



    useEffect(() => {

        setAllMessages([]); // 채팅방이 변경될 때 이전 메시지 초기화
        loadMessages();

        subscribe(`/sub/chat-rooms/${params.roomId}`, (msg) => {
            if(msg.senderName === "SYSTEM" && msg.content ==="비속어 탐지 중..."){
                setIsDetectingBadWord(true);
            } else if(msg.senderName==="SYSTEM" && msg.content===""){
                setIsDetectingBadWord(false);
            } else {
                setAllMessages(prev => [...prev, msg]);
            }
        });
        subscribe(`/sub/warnings/users/${userInfo.id}`, (warning) => {
            console.log("warning : ", warning);
            alert(warning.content);
        });

        return () => {
            unsubscribe(`/sub/chat-rooms/${params.roomId}`);
            unsubscribe(`/sub/warnings/users/${userInfo.id}`);
        };
    }, [params.roomId]);

    return (
        <div className='card'>
            <header className='card-header'>
                <div>
                    <h4>{isGroup ? groupChatRoomName : friendName || "테스트용 방 이름"}</h4>
                    <p>방번호: {params.roomId}</p>
                    {!isGroup && friendEmail && <p>상대방 이메일: {friendEmail}</p>}
                </div>
            </header>
            <div className='card-body'>
                {
                    isDetectingBadWord && (
                        <div className="system-message">
                            <p>비속어 탐지 중...</p>
                        </div>
                    )
                }
                {allMessages.map((msg) => (
                    <ChatMessage
                        key={msg.id}
                        message={msg}
                        userId={userInfo.id}
                    />
                ))}
            </div>
            <div className='card-footer'>
                <input
                    type='text'
                    placeholder='메시지를 입력하세요.'
                    className='form-control form-control-lg'
                    name='content'
                    value={message.content}
                    onChange={handleOnChange}/>
                <button className='btn btn-primary' type='button' onClick={handleSendMessage}>전송</button>
            </div>
        </div>
    );
}

export default MessagePage