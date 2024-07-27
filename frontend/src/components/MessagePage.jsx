import React, { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import Avatar from './Avatar';
import Message from './Message';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { sendMessage, activateClient, subscribeToRoom } from '../stomp';
import { getAllMessagesByRoomId } from '../api/chatRoomApi';

const MessagePage = () => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const [allMessages, setAllMessages] = useState([]);
  const [message, setMessage] = useState({
    senderId: userInfo.id,
    roomId: params.roomId,
    content: '',
    type: 'CHAT',
  });

  const subscriptionRef = useRef(null);

  useEffect(() => {
    setAllMessages([]); // 채팅방이 변경될 때 이전 메시지 초기화

    activateClient();

    if(subscriptionRef.current){
      subscriptionRef.current.unsubscribe();
    }

    subscriptionRef.current = subscribeToRoom(params.roomId, msg => {
      setAllMessages(prev => [...prev, JSON.parse(msg.body)]);
    });

    const loadMessages = async () => {
      try{
        const res = await getAllMessagesByRoomId({roomId: params.roomId});
        if(res.data.length !== 0){
          setAllMessages(res.data);
        }
      }catch(err){
        console.error("Error loading messages: ", err);
      }
    };

    loadMessages();

    return () => {
      if(subscriptionRef.current){
        subscriptionRef.current.unsubscribe();
      }
    };
  }, [params.roomId]);

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setMessage(prev => {
      return{
        ...prev,
        [name]: value,
      }
    });
  };

  const handleSendMessage = () => {
    sendMessage(params.roomId, message);
    setMessage({
      senderId: userInfo.id,
      roomId: params.roomId,
      content: '',
      type: 'CHAT',
    });
  };

  return (
    <div className='card'>
      <header className='card-header'>
        <div>
          <Avatar width={50} height={50}/>
          <p>이름</p>
        </div>
      </header>
      <div className='card-body'>
        {
          allMessages.map((msg, index) => {
            return <Message message={msg} key={index} userId={userInfo.id}/>
          })
        }
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