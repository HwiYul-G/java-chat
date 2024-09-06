import {useEffect, useState} from 'react';
import {useUser} from '../../../context/UserContext.js';
import '../css/_common.css';
import GroupChat from './GroupChat.jsx';
import {useNavigate} from 'react-router-dom';
import {getGroupChatRooms} from "../../../api/userApi";

const GroupChatList = () => {
    const {userInfo} = useUser();

    const [message, setMessage] = useState('');
    const [groupChats, setGroupChats] = useState([]);

    const navigate = useNavigate();

    const fetchGroupChats = async () => {
        try {
            const groupChats = await getGroupChatRooms(userInfo.id); // 수정된 부분
            if (groupChats.length === 0) {
                setMessage('그룹 채팅을 시작해보세요!');
            } else {
                setMessage('');
                setGroupChats(groupChats);
            }
        } catch (err) {
            setMessage(err.message);
        }
    };

    useEffect(() => {
        fetchGroupChats();
    }, [userInfo.id]);

    return (
        <div>
            {message && <div>{message}</div>}
            <ul className='list-unstyled js-contact-list mb-0'>
                {groupChats.map(groupChat => (
                    <li
                        key={groupChat.roomId}
                        className='card contact-item'
                        style={{cursor: 'pointer'}}
                        onClick={() => navigate(`chat-rooms/${groupChat.groupChatRoomId}`, {
                            state: {
                                groupChatRoomName: groupChat.chatRoomInfo.roomName
                            }
                        })}
                    >
                        <GroupChat roomId={groupChat.roomId} roomName={groupChat.chatRoomInfo.roomName} lastMessage={groupChat.lastMessageInfo.content}/>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default GroupChatList;