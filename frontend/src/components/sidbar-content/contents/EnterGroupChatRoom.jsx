const EnterGroupChatRoom = () => {
    return (
        <div className="card mb-3">
            <div className="card-body pt-0">
                <form>
                    <input type="text" placeholder="채팅방 번호를 입력해주세요."
                    className="form-control form-control-sm form-control-solid"/>
                    <button className="btn btn-primary">
                        참여
                    </button>
                </form>
            </div>
        </div>
    );
};

export default EnterGroupChatRoom;