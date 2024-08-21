import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import Avatar from './Avatar';
import PersonalMessage from './PersonalMessage';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { getAllMessagesByPersonalRoomId  } from '../api/personalChatRoomApi';
import { WebSocketContext } from '../context/WsContext';

const PersonalMessagePage = () => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const { subscribe, unsubscribe, publish } = useContext(WebSocketContext)
  ;
  const [allPersonalMessages, setAllPersonalMessages] = useState([]);
  const [personalMessage, setPersonalMessage] = useState({
    senderId: userInfo.id,
    roomId: params.roomId,
    content: '',
    type: 'CHAT',
  });

  const location = useLocation();
  const {friendName, friendEmail} = location.state || {};

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setPersonalMessage(prev => ({ ...prev, [name]: value }));
  };

  const handleSendMessage = () => {
    if (personalMessage.content === '') return;
    publish(`/pub/personal-chat-rooms/${params.roomId}`, personalMessage);
    setPersonalMessage({
      senderId: userInfo.id,
      roomId: params.roomId,
      content: '',
      type: 'CHAT',
    });
  };

  useEffect(() => {
    setAllPersonalMessages([]);

    const loadMessages = async () => {
      try{
        const res = await getAllMessagesByPersonalRoomId({roomId: params.roomId});
        if(res.data.length !== 0){
          setAllPersonalMessages(res.data);
        }
      }catch(err){
        console.error("Error loading messages: ", err);
      }
    };

    loadMessages();

    subscribe(`/sub/personal-chat-rooms/${params.roomId}`, (msg) => {
      setAllPersonalMessages(prev => [...prev, msg]);
    });

    return () => {
      unsubscribe(`/sub/personal-chat-rooms/${params.roomId}`);
    };
  }, [params.roomId]);

  return (
    <div className='card'>
      <header className='card-header'>
        <div>
          <Avatar width={48} height={48}/>
          <p>{friendName}</p>
          <p>{friendEmail}</p>
        </div>
      </header>
      <div className='card-body'>
        {
          allPersonalMessages.map((msg, index) => {
            return <PersonalMessage message={msg} key={index} userId={userInfo.id}/>
          })
        }
      </div>
      <div className='card-footer'>
        <input 
          type='text' 
          placeholder='메시지를 입력하세요.'
          className='form-control form-control-lg'
          name='content'
          value={personalMessage.content} 
          onChange={handleOnChange}/>
        <button className='btn btn-primary' type='button' onClick={handleSendMessage}>전송</button>
      </div>
    </div>
  );
}

export default PersonalMessagePage