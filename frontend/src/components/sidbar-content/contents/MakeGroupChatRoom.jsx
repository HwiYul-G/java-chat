import {useState} from "react";

import {useUser} from "../../../context/UserContext";
import {useNavigate} from "react-router-dom";
import {createChatRoom} from "../../../api/chatApi";

const MakeGroupChatRoom = () => {
    const {userInfo} = useUser();
    const [roomName, setRoomName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [roomId, setRoomId] = useState();

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        e.stopPropagation();
        try {
            const result = await createChatRoom({
                isGroup: true, // 그룹 채팅방을 생성하기 위한 설정
                userId: userInfo.id,
                roomName: roomName,
                friendId: null // 그룹 채팅방에서는 친구 ID가 필요하지 않으므로 null로 설정
            });
            if (result.flag) {
                setRoomId(result.data.roomId);
                setRoomName(result.data.chatRoomInfo.roomName);
                navigate(`/chat-rooms/${result.data.roomId}`, {
                    state: {
                        isGroup: true,
                        groupChatRoomName: roomName
                    }
                });
            } else {
                setErrorMessage(result.message);
            }
        } catch (err) {
            setErrorMessage(err.message);
        }
    };

    return (

        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input type="text" placeholder="그룹 채팅방 이름을 입력하세요." value={roomName} onChange={(e) => {
                        setRoomName(e.target.value)
                    }}
                           className="form-control form-control-sm form-control-solid" required/>
                    <button className="btn btn-primary" onClick={handleSubmit}>
                        생성
                    </button>
                </form>
                {errorMessage && <p>Error: {errorMessage}</p>}
                {roomId && <p>생성된 방 번호: {roomId}</p>}
            </div>
        </div>

    );
};

export default MakeGroupChatRoom;