import instance from ".";

const prefix = '/group-chat-rooms'

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

export const getAllMessagesByGroupRoomId = async({roomId}) => {
    try{
        const res = await instance.get(`${prefix}/${roomId}/messages`);
        return res.data;
    }catch(err){
        console.error(`${roomId} 번 방의 메시지 가져오기 실패: `, err);
        throw err;
    }
};