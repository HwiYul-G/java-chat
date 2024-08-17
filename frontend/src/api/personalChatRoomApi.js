import instance from ".";

const prefix = '/personal-chat-rooms'

export const getAllMessagesByPersonalRoomId = async({roomId}) => {
    try{
        const res = await instance.get(`${prefix}/${roomId}/messages`);
        return res.data;
    }catch(err){
        console.error(`개인채팅방의 ${roomId} 번 방의 메시지 가져오기 실패: `, err);
        throw err;
    }
};

export const findPersonalChatRoomsByUserId = async(userId) => {
    try{
        const res = await instance.get(`${prefix}/users/${userId}`);
        console.log(res.data);
        return res.data;
    }catch(err){
        console.error("서버의 개인 채팅룸 목록 API 호출 실패: ", err);
        throw err;
    }
};

export const makePersonalChatRoom = async({myId, friendId}) => {
    const data = {userId1: myId, userId2: friendId};
    try{
        const res = await instance.post(`${prefix}`, data);
        console.log(res);
    }catch(err){
        console.error("서버의 개인 채팅방 만들기 API 호출 실패: ", err);
        throw err;
    }
};
