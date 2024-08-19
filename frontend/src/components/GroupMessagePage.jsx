import React, { useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import GroupMessage from './GroupMessage';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { getAllMessagesByGroupRoomId } from '../api/groupChatRoomApi';
import * as StompJs from '@stomp/stompjs';

const GroupMessagePage = () => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const [allGroupMessages, setAllGroupMessages] = useState([]);
  const [groupMessage, setGroupMessage] = useState({
    senderId: userInfo.id,
    roomId: params.roomId,
    senderName: userInfo.username,
    content: '',
    type: 'CHAT',
  });

  const location = useLocation();
  const {groupChatRoomName} = location.state || {};

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
        const dest = `/sub/group-chat-rooms/${params.roomId}`;
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
      setAllGroupMessages((groupMessages) => [...groupMessages, msg]);
    }
  };

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setGroupMessage(prev => {
      return{
        ...prev,
        [name]: value,
      }
    });
  };

  const handleSendMessage = (message) => {
    if(message === '')
      return;
    client.publish({
      destination: `/pub/group-chat-rooms/${params.roomId}`,
      body: JSON.stringify(message),
      headers: {
        'content-type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    });
    setGroupMessage({
      senderId: userInfo.id,
      roomId: params.roomId,
      senderName: userInfo.username,
      content: '',
      type: 'CHAT',
    });
  };

  useEffect(() => {
    setAllGroupMessages([]); // 채팅방이 변경될 때 이전 메시지 초기화

    connect();

    const loadMessages = async () => {
      try{
        const res = await getAllMessagesByGroupRoomId({roomId: params.roomId});
        if(res.data.length !== 0){
          setAllGroupMessages(res.data);
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
          <h4>{groupChatRoomName ? groupChatRoomName : "테스트용 방 이름"}</h4>
          <p>방번호: {params.roomId}</p>
        </div>
      </header>
      <div className='card-body'>
        {
          allGroupMessages.map((msg, index) => {
            return <GroupMessage message={msg} key={index} userId={userInfo.id} roomId={params.id} senderName={msg.senderName} />
          })
        }
      </div>
      <div className='card-footer'>
        <input 
          type='text' 
          placeholder='메시지를 입력하세요.'
          className='form-control form-control-lg'
          name='content'
          value={groupMessage.content} 
          onChange={handleOnChange}/>
        <button className='btn btn-primary' type='button' onClick={() => handleSendMessage(groupMessage)}>전송</button>
      </div>
    </div>
  );
}

export default GroupMessagePage