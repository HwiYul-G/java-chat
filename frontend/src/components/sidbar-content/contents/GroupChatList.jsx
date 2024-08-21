import { useEffect, useState } from 'react';
import { useUser } from '../../../context/UserContext.js';
import '../css/_common.css';
import GroupChat from './GroupChat.jsx';
import { findGroupChatRoomsByUserid } from '../../../api/groupChatRoomApi.js';
import { useNavigate } from 'react-router-dom';

const GroupChatList = () => {
    const {userInfo} = useUser();

    const [message, setMessage] = useState('');
    const [groupChats, setGroupChats] = useState([]);

    const navigate = useNavigate();

    useEffect(()=>{
        const fetchGroupChats = async () => {
            try{
                const groupChats = await findGroupChatRoomsByUserid(userInfo.id);
                if(groupChats.flag && groupChats.data.length === 0){
                    setMessage('그룹 채팅을 시작해보세요!');
                }
                if(groupChats.flag && groupChats.data.length !== 0){
                    setMessage('');
                    setGroupChats(groupChats.data);
                }
            }catch(err){
                setMessage(err.message);
            }
        };
        fetchGroupChats();
    }, [userInfo.id]);

    return (
        <div>
            {message && <div>{message}</div>}
            <ul className='list-unstyled js-contact-list mb-0'>
                { groupChats.map(groupChat => (
                    <li 
                        key={groupChat.groupChatRoomId} 
                        className='card contact-item'
                        style={{cursor: 'pointer'}}
                        onClick={()=> navigate(`group/${groupChat.groupChatRoomId}`, {
                            state: {
                                groupChatRoomName: groupChat.groupChatRoomName
                            }
                        })}
                    >
                        <GroupChat {...groupChat}/>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default GroupChatList;