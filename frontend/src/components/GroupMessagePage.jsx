import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import GroupMessage from './GroupMessage';
import './css/_messagePage.css';
import { useUser } from '../context/UserContext';
import { getAllMessagesByGroupRoomId } from '../api/groupChatRoomApi';
import { WebSocketContext } from '../context/WsContext';

const GroupMessagePage = () => {
  const {userInfo} = useUser();
  const params = useParams();  // console.log("params : ", params.roomId);
  const { subscribe, unsubscribe, publish } = useContext(WebSocketContext);

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

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setGroupMessage(prev => ({ ...prev, [name]: value }));
  };

  const handleSendMessage = (message) => {
    if(message === '')
      return;
    publish(`/pub/group-chat-rooms/${params.roomId}`, groupMessage);
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

    subscribe(`/sub/group-chat-rooms/${params.roomId}`, (msg) => {
      setAllGroupMessages(prev => [...prev, msg]);
    });
  
    return () => {
      unsubscribe(`/sub/group-chat-rooms/${params.roomId}`);
    };
  }, [params.roomId] );

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
        <button className='btn btn-primary' type='button' onClick={handleSendMessage}>전송</button>
      </div>
    </div>
  );
}

export default GroupMessagePage