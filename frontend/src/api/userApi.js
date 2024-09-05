import instance from ".";

const prefix = "/users";

export const register = async (newUser) => {
    try{
        // const newUser = {"email": , "username": , "password": , "roles": "user"}
        const res = await instance.post(`${prefix}`, newUser);
        console.log(res.data);
        return res.data;
    }catch(err){
        console.error("서버의 회원가입 API 호출 실패: ", err);
        throw err;
    }
};

export const updateUserInfo = async (userId, userInfo) => {
    try{
        // {email: "", username:"" , roles: "user"}
        const res = await instance.put(`${prefix}/${userId}`, userInfo);
        console.log(res.data);
        return res.data;
    } catch(err){
        console.error("서버의 사용자 정보 업데이트 API 호출 실패: ", err);
        throw err;
    }
};

export const deleteAccount = async (userId) => {
    try{
        const res = await instance.delete(`${prefix}/${userId}`);
        console.log(res.data);
        return res.data;
    } catch(err){
        console.error("서버의 탈퇴하기 API 호출 실패: ", err);
        throw err;
    }
};

export const getFriends = async (userId) => {
    try{
        const res = await instance.get(`${prefix}/${userId}/friends`);
        console.log(res.data);
        return res.data;
    }catch(err){
        console.error("서버의 친구 목록 조회 API 호출 실패: ", err);
        throw err;
    }
};

export const getNotifications = async (userId) => {
    try{
        const res = await instance.get(`${prefix}/${userId}/notifications`);
        console.log(res.data);
        return res.data;
    }catch (err){
        console.error("서버의 알림 목록 조회 API 호출 실패: ", err);
        throw err;
    }
};

const getChatRooms = async(userId) => {
    try{
        const res = await instance.get(`${prefix}/${userId}/chat-rooms`);
        console.log(res.data);
        return res.data;
    } catch(err){
        console.error("서버의 채팅방 목록 조회 API 호출 실패: ", err);
        throw err;
    }
};

export const getPersonalChatRooms = async (userId) => {
    const chatRooms = await getChatRooms(userId);
    return chatRooms.data.filter(room => !room.isGroup);
};

export const getGroupChatRooms = async (userId) => {
    const chatRooms = await getChatRooms(userId);
    return chatRooms.data.filter(room => room.isGroup);
};