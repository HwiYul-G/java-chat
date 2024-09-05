import instance from "./index";

const prefix = '/chat-rooms';

export const getMessagesByRoomId = async (roomId) => {
    try{
        const res = await instance.get(`${prefix}/${roomId}/messages`);
        console.log(res);
        return res.data;
    }catch(err){
        console.error(err);
        throw err;
    }
};

export const createChatRoom = async (roomInfo) => {
    // {"isGroup": , "userId": , "roomName": , "friendId": }
    try{
        const res = await instance.post(`${prefix}`, roomInfo);
        return res.data;
    } catch(err){
        console.error(err);
        throw err;
    }
};

export const enterGroupChatRoom = async (roomId, userId) => {
    const requestData =  {"userId": userId, "isGroup": true}
    try{
        const res = await instance.post(`${prefix}/${roomId}/enter`, requestData);
        return res.data;
    } catch (err){
        console.error(err);
        throw err;
    }
};
