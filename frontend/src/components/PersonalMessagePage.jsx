import React, { useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import Avatar from './Avatar';
import PersonalMessage from './PersonalMessage';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { getAllMessagesByPersonalRoomId  } from '../api/personalChatRoomApi';
import * as StompJs from '@stomp/stompjs';

const PersonalMessagePage = () => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const [allPersonalMessages, setAllPersonalMessages] = useState([]);
  const [personalMessage, setPersonalMessage] = useState({
    senderId: userInfo.id,
    roomId: params.roomId,
    content: '',
    type: 'CHAT',
  });

  const location = useLocation();
  const {friendName, friendEmail} = location.state || {};

  let [client, changeClient] = useState(null);

  const connect = () => {
    try{
      const clientdata = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/java-chat',
        connectHeaders: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        debug: function(str){
          // console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });
      
      // 구독
      clientdata.onConnect = function(){
        const dest = `/sub/personal-chat-rooms/${params.roomId}`;
        clientdata.subscribe(dest, callback);
      };

      clientdata.activate(); // 클라이언트 활성화
      changeClient(clientdata); // 클라이언트 갱신
    }catch(err){
      console.error(err);
    }
  };

  const disConnect = () => {
    if(client === null)
      return;
    client.deactivate();
  };

  const callback = (message) => {
    if(message.body){
      let msg = JSON.parse(message.body);
      setAllPersonalMessages((personalMessages) => [...personalMessages, msg]);
    }
  };

  const handleSendMessage = (message) => {
    if(message === '')
      return;
    client.publish({
      destination: `/pub/personal-chat-rooms/${params.roomId}`,
      body: JSON.stringify(message),
      headers: {
        'content-type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    });

    setPersonalMessage({
      senderId: userInfo.id,
      roomId: params.roomId,
      content: '',
      type: 'CHAT',
    });
  };

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setPersonalMessage(prev => {
      return{
        ...prev,
        [name]: value,
      }
    });
  };

  useEffect(() => {
    setAllPersonalMessages([]);
    
    connect();

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
      disConnect();
    };
  }, []);

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
        <button className='btn btn-primary' type='button' onClick={() => handleSendMessage(personalMessage)}>전송</button>
      </div>
    </div>
  );
}

export default PersonalMessagePage