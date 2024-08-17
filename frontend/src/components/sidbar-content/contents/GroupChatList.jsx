import { useEffect, useState } from 'react';
import { useUser } from '../../../context/UserContext.js';
import '../css/_common.css';
import GroupChat from './GroupChat.jsx';
import { findGroupChatRoomsByUserid } from '../../../api/groupChatRoomApi.js';

const GroupChatList = () => {
    const {userInfo} = useUser();

    const [message, setMessage] = useState('');
    const [groupChats, setGroupChats] = useState([]);

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
                    <li key={groupChat.groupChatRoomId} className='card contact-item'>
                        <GroupChat {...groupChat}/>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default GroupChatList;