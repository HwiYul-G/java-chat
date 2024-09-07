import './css/_common.css';
import './contents/Friend';
import Friend from './contents/Friend';
import {useEffect, useState} from 'react';
import AddFriendModal from './contents/AddFriendModal';
import {useUser} from '../../context/UserContext';
import {useNavigate} from 'react-router-dom';
import {getFriends} from "../../api/userApi";
import {createChatRoom} from "../../api/chatApi";

const FriendListSidebar = () => {
    const {userInfo} = useUser();

    const [showModal, setShowModal] = useState(false);
    const [friends, setFriends] = useState([]);

    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const fetchFriends = async () => {
        try {
            const friendsData = await getFriends(userInfo.id);
            if (friendsData.flag && friendsData.data.length === 0) {
                setMessage("친구를 추가해 보세요");
            }
            if (friendsData.flag && friendsData.data.length !== 0) {
                setMessage('');
                setFriends(friendsData.data);
            }
        } catch (err) {
            setMessage(err.message);
        }
    };

    useEffect(() => {
        fetchFriends();
    }, [userInfo.id]);

    const handleShowModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleFriendClicked = async (roomId, friendId, friendName, friendEmail) => {
        if (roomId === undefined || roomId === null) {
            const res = await createChatRoom({
                "isGroup": false,
                "userId": userInfo.id,
                "roomName": "",
                "friendId": friendId
            });
            if (res.flag) {
                navigate(`chat-rooms/${res.data.roomId}`, {state: {friendName, friendEmail}});
            }
        } else {
            navigate(`chat-rooms/${roomId}`, {state: {friendName, friendEmail}});
        }
    };


    return (
        <div className='tab-pane'>
            <div className='d-flex flex-column h-100'>
                <div className='tab-header'>
                    <h5>친구 목록</h5>
                </div>

                {
                    message && (
                        <div className='alert alert-danger'>
                            {message}
                        </div>
                    )
                }

                <div className='hide-scrollbar h-100'>
                    <div className='m-4'>
                        <ul className='list-unstyled'>
                            {friends.map(friend => (
                                <li
                                    className='card contact-item'
                                    key={friend.userId}
                                    style={{cursor: 'pointer'}}
                                    onClick={() => handleFriendClicked(friend.roomId, friend.userId, friend.name, friend.email)}
                                >
                                    <Friend {...friend}/>
                                </li>
                            ))
                            }
                        </ul>
                    </div>
                </div>

                <div className='tab-footer border-top p-4'>
                    <button className='btn btn-sm btn-primary w-100' onClick={handleShowModal}>친구 추가</button>
                </div>
            </div>
            <AddFriendModal show={showModal} onClose={handleCloseModal}/>
        </div>
    );
};

export default FriendListSidebar;