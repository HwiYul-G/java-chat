import React, { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import Avatar from './Avatar';
import PersonalMessage from './PersonalMessage';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { sendMessage, activateClient, subscribeToPersonalRoom, sendPersonalMessage} from '../stomp';
import { getAllMessagesByPersonalRoomId  } from '../api/personalChatRoomApi';

const PersonalMessagePage = ({friendName}) => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const [allPersonalMessages, setAllPersonalMessages] = useState([]);
  const [personalMessage, setPersonalMessage] = useState({
    senderId: userInfo.id,
    roomId: params.roomId,
    content: '',
    type: 'CHAT',
  });

  const subscriptionRef = useRef(null);

  useEffect(() => {
    setAllPersonalMessages([]); // 채팅방이 변경될 때 이전 메시지 초기화

    activateClient();

    if(subscriptionRef.current){
      subscriptionRef.current.unsubscribe();
    }

    subscriptionRef.current = subscribeToPersonalRoom(params.roomId, msg => {
      setAllPersonalMessages(prev => [...prev, JSON.parse(msg.body)]);
    });

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

    return () => {
      if(subscriptionRef.current){
        subscriptionRef.current.unsubscribe();
      }
    };
  }, [params.roomId]);

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setPersonalMessage(prev => {
      return{
        ...prev,
        [name]: value,
      }
    });
  };

  const handleSendMessage = () => {
    sendPersonalMessage(params.roomId, personalMessage);
    setPersonalMessage({
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
          <Avatar width={48} height={48}/>
          <p>{friendName}</p>
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