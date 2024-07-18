
const MakeGroupChatRoom = () => {
    return (
        
        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input type="text" placeholder="그룹 채팅방 이름을 입력하세요."
                    className="form-control form-control-sm form-control-solid"/>
                    <button className="btn btn-primary">
                        생성
                    </button>
                </form>
            </div>
        </div>
    
    );
};

export default MakeGroupChatRoom;