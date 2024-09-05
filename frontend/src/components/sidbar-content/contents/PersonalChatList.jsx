import {useEffect, useState} from 'react';
import {useUser} from '../../../context/UserContext';
import '../css/_common.css';
import PersonalChat from './PersonalChat';
import {useNavigate} from 'react-router-dom';
import {getPersonalChatRooms} from "../../../api/userApi";

const PersonalChatList = () => {
    const {userInfo} = useUser();
    const [personalChats, setPersonalChats] = useState([]);
    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    const fetchPersonalChats = async () => {
        try {
            const personalChats = await getPersonalChatRooms(userInfo.id);
            if (personalChats.length === 0) {
                setMessage('친구와 채팅을 해보세요!');
            } else {
                setMessage('');
                setPersonalChats(personalChats);
            }
        } catch (err) {
            setMessage(err.message);
        }
    };

    useEffect(() => {
        fetchPersonalChats();
    }, [userInfo.id]);

    return (
        <div>
            {message && (
                <div className='alert alert-danger'>
                    {message}
                </div>
            )}

            <ul className="list-unstyled js-contact-list mb-0">
                {personalChats.map(personalChat => (
                    <li
                        key={personalChat.friendInfo.email}
                        className="card contact-item"
                        style={{cursor: 'pointer'}}
                        onClick={() => navigate(`chat-rooms/${personalChat.roomId}`, {
                            state: {
                                friendName: personalChat.friendInfo.name,
                                friendEmail: personalChat.friendInfo.email
                            }
                        })}
                    >
                        <PersonalChat {...personalChat} />
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PersonalChatList;