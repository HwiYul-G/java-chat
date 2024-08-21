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

export const findGroupChatRoomsByUserid = async(userId) => {
    try{
        const res = await instance.get(`${prefix}/users/${userId}`);
        return res.data;
    }catch(err){
        console.error(`${userId}번 사용자의 그룹 채팅 목록 가져오기 API 호출 실패: `, err);
        throw err;
    }
};