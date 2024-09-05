import {useState} from "react";
import {useUser} from "../../../context/UserContext";
import {useNavigate} from "react-router-dom";
import {enterGroupChatRoom} from "../../../api/chatApi";

const EnterGroupChatRoom = () => {
    const {userInfo} = useUser();
    const [roomId, setRoomId] = useState();
    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    const handleEnterClicked = async (e) => {
        e.preventDefault();
        e.stopPropagation();
        try {
            const result = await enterGroupChatRoom(roomId, userInfo.id);

            if (result.flag) {
                const {isGroup, chatRoomInfo} = result.data;

                if (isGroup) {
                    navigate(`chat-rooms/${result.data.roomId}`, {
                        state: {
                            groupChatRoomName: chatRoomInfo.roomName
                        }
                    });
                } else {
                    setMessage('그룹 채팅방이 아닙니다.');
                }
            } else {
                setMessage('채팅방 입장에 실패했습니다.');
            }
        } catch (err) {
            setMessage(err.message);
        }
    };

    return (
        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input
                        type="text"
                        placeholder="채팅방 번호를 입력해주세요."
                        className="form-control form-control-sm form-control-solid"
                        value={roomId}
                        onChange={(e) => {setRoomId(e.target.value)}}
                    />
                    <button className="btn btn-primary" onClick={handleEnterClicked}>
                        참여
                    </button>
                </form>
                {message && <p>Error: {message}</p>}
            </div>
        </div>
    );
};

export default EnterGroupChatRoom;