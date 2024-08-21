import instance from ".";

const prefix = '/group-chat-join'

export const enterGroupChatRoom = async({roomId, userId}) => {
    const data = {
        userId: userId,
        roomId: roomId
    };
    
    try{
        const res = await instance.post(`${prefix}`, data);
        return res.data;
    }catch(err){
        console.error("서버의 그룹 채팅방 참여하기 API 호출 실패: ", err);
        throw err;
    }
};