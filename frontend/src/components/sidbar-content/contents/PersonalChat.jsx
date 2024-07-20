import Avatar from "../../Avatar.jsx";

const PersonalChat = ({userId, name, lastMessage, imgUrl}) => {
    return (
        <div className="card-body">
            <div className="d-flex align-items-center">
                <Avatar width={40} height={40} imageUrl={imgUrl}/>
                <div className="flex-grow-1 overflow-hidden">
                    <h6>{name}</h6>
                </div>
                <div className="d-flex align-items-center">
                    <div className="line-clamp me-auto">{lastMessage}</div>
                </div>
            </div>
        </div>
    );
};

export default PersonalChat;