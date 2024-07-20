import '../css/_common.css';
import GroupChat from './GroupChat.jsx';

const GroupChatList = () => {
    const groupChats = [
        {roomId: 1, roomName: '방이름1', lastMessage:'방 1에서 보낸 마지막 메시지'},
        {roomId: 2, roomName: '방이름2',  lastMessage:'쿠버네티스 코리아 2024 커뮤니티 데이 홍보'},
    ];

    return (
        <div>
            <ul className='list-unstyled js-contact-list mb-0'>
                { groupChats.map(groupChat => (
                    <li key={groupChat.roomId} className='card contact-item'>
                        <GroupChat {...groupChat}/>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default GroupChatList;