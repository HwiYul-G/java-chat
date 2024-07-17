import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import Avatar from './Avatar';
import Message from './Message';
import './css/_messagePage.css';

const MessagePage = () => {
  const params = useParams();  // console.log("params : ", params.userId);
  // userId는 상대방의 정보임
  const [allMessages, setAllMessages] = useState([]);
  const [message, setMessage] = useState({
    senderId: params.userId,
    roomId: params.roomId,
    content: '',
    type: 'CHAT',
  });

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setMessage(prev => {
      return{
        ...prev,
        [name]: value,
      }
    });
  };

  // 임시 데이터 설정
  useEffect(() => {
    const tempMessages = [
      {
        id: 1,
        senderId: params.userId,
        roomId: 1,
        content: 'Hello!ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd',
        createdAt: new Date().toISOString(),
        type: 'CHAT'
      },
      {
        id: 2,
        senderId: 2,
        roomId: 1,
        content: 'Hi there!',
        createdAt: new Date().toISOString(),
        type: 'CHAT'
      },
      {
        id: 3,
        senderId: 1,
        roomId: 1,
        content: 'How are you?',
        createdAt: new Date().toISOString(),
        type: 'CHAT'
      },
    ];
    setAllMessages(tempMessages);
  }, []);



  return (
    <div className='card'>
      <header className='card-header'>
        <Avatar width={50} height={50}/>
        <p>
          이름
        </p>
      </header>
      <div className='card-body'>
        {
          allMessages.map((msg, key) => {
            return <Message message={msg} key={key} userId={params.userId}/>
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
        <button className='btn btn-primary' type='button'>전송</button>
      </div>
    </div>
  );
}

export default MessagePage