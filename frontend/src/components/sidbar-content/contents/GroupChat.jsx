import '../css/_groupChat.css';

const GroupChat = ({roomId, roomName, lastMessage}) => {
    return (
        <div className='card-body'>
            <div className='d-flex flex-column align-items-start'>
                <div className='room-name mb-2'>
                    <h5>{roomName}</h5>
                </div>

                <div className="last-message">
                    <div className="line-clamp me-auto">{lastMessage}</div>
                </div>
            </div>
        </div>
    );
};
export default GroupChat;