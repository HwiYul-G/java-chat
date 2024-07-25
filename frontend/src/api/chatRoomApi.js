import instance from ".";

const prefix = '/chat-rooms'

export const makeGroupChatRoom = async({roomName, userId}) => {
    const data = {name: roomName, managerUserId: userId, isGroup: true};
    try{
        const res = await instance.post(`${prefix}`, data);
        return res.data;
    }catch(err){
        console.error("서버의 채팅방 추가 API 호출 실패: ", err);
        throw err;
    }
};