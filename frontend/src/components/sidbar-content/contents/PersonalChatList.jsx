import '../css/_common.css';
import PersonalChat from './PersonalChat';

const PersonalChatList = () => {

    const chats = [
        { userId: 1, name: 'Alice', lastMessage: '안녕하세요!', imgUrl: '' },
        { userId: 2, name: 'Bob', lastMessage: '오늘 점심 어때요?', imgUrl: '' },
        { userId: 3, name: 'Charlie', lastMessage: '무슨 계획 있어요?', imgUrl: '' }
    ];

    return (
        <div>
            <ul className="list-unstyled js-contact-list mb-0">
                {chats.map(chat => (
                    <li key={chat.userId} className="card contact-item">
                        <PersonalChat {...chat} />
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PersonalChatList;