import { useEffect, useState } from 'react';
import { useUser } from '../../../context/UserContext';
import '../css/_common.css';
import PersonalChat from './PersonalChat';
import { findPersonalChatRoomsByUserId } from '../../../api/personalChatRoomApi';

const PersonalChatList = () => {
    const {userInfo} = useUser();
    
    const [personalChats, setPersonalChats] = useState([]);
    const [message, setMessage] = useState('');
    
    useEffect(() => {
        const fetchPersonalChats = async () => {
            try {
                const personalChats = await findPersonalChatRoomsByUserId(userInfo.id);
                if(personalChats.flag && personalChats.data.length === 0){
                    setMessage('친구와 채팅을 해보세요!')
                }
                if(personalChats.flag && personalChats.data.length !== 0){
                    console.log(personalChats.data);
                    setMessage('');
                    setPersonalChats(personalChats.data);
                }
            } catch(err) {
                setMessage(err.message);
            }
        };
        fetchPersonalChats();
    }, [userInfo.id]);

    return (
        <div>
            {
                message && (
                    <div className='alert alert-danger'>
                        {message}
                    </div>
                )
            }

            <ul className="list-unstyled js-contact-list mb-0">
                {personalChats.map(personalChat => (
                    <li key={personalChat.friendId} className="card contact-item">
                        <PersonalChat {...personalChat} />
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PersonalChatList;