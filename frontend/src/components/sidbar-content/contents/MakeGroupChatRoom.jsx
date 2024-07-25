import { useState } from "react";
import { makeGroupChatRoom } from "../../../api/chatRoomApi";
import { useUser } from "../../../context/UserContext";
import { useNavigate } from "react-router-dom";

const MakeGroupChatRoom = () => {

    const {userInfo} = useUser(); 
    const [roomName, setRoomName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [roomId, setRoomId] = useState();

    const navigate = useNavigate();

    const handleSubmit = async(e) => {
        e.preventDefault();
        e.stopPropagation();
        try{
            const result = await makeGroupChatRoom({roomName: roomName, userId: userInfo.id});
            if(result.flag){
                setRoomId(result.data.id);
                navigate(`/${result.data.id}`);
            }
        }catch(err){
            setErrorMessage(err.message);
        }
    };

    return (
        
        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input type="text" placeholder="그룹 채팅방 이름을 입력하세요." value={roomName} onChange={(e) => {setRoomName(e.target.value)}}
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