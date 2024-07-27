import { useState } from "react";
import { useUser } from "../../../context/UserContext";
import { useNavigate } from "react-router-dom";
import { enterGroupChatRoom } from "../../../api/chatJoinApi";

const EnterGroupChatRoom = () => {
    const {userInfo} = useUser();
    const [roomId, setRoomId] = useState();
    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    const handleEnterClicked = async(e) => {
        e.preventDefault();
        e.stopPropagation();
        try{
            const result = await enterGroupChatRoom({roomId: roomId, userId: userInfo.id});
            if(result.flag){
                navigate(`/${roomId}`);
            }
        }catch(err){
            setMessage(err.message);
        }
    };

    return (
        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input type="text" placeholder="채팅방 번호를 입력해주세요."
                    className="form-control form-control-sm form-control-solid"
                    value={roomId} onChange={(e) => {setRoomId(e.target.value)}}/>
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