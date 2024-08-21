import instance from ".";

const prefix = "/users";

export const register = async (newUser) => {
    try{
        const res = await instance.post(`${prefix}`, newUser);
        return res.data;
    }catch(err){
        console.log("서버의 회원가입 API 호출 실패: ", err);
        throw err;
    }
};

export const updateUserInfo = async (userId, userInfo) => {
    try{
        // {email, username, enabled , roles}
        const res = await instance.put(`${prefix}/${userId}`, userInfo);
        return res.data;
    } catch(err){
        console.log("서버의 사용자 정보 업데이트 API 호출 실패: ", err);
        throw err;
    }
};

export const deleteAccount = async (userId) => {
    try{
        const res = await instance.delete(`${prefix}/${userId}`);
        return res.data;
    } catch(err){
        console.log("서버의 탈퇴하기 API 호출 실패: ", err);
        throw err;
    }
};

export const findUserByEmail = async (email) => {
    try{
        const res = await instance.get(`${prefix}/email`, {
            params: {email}
        });
        return res.data;
    } catch(err){
        console.log('서버의 이메일로 사용자 찾기 API 호출 실패: ',err);
        throw err;
    }
};

export const findFriendsByUserId = async(userId) => {
    try {
        const res = await instance.get(`${prefix}/${userId}/friends`)
        return res.data;
    } catch(err){
        console.error("서버의 친구 목록 가져오기 API 호출 실패: ", err);
        throw err;
    }
}

export const inviteFriend = async(data) => {
    // {userId: , friendEmail: }
    try{
        const res = await instance.post(`${prefix}/invite-friend`, data);
        console.log(res);
        return res.data;
    } catch(err){
        console.error("서버의 친구 초대하기 API 호출 실패: ", err);
        throw err;
    }
};

export const acceptFriendRequest = async(friendRequestId) => {
    try{
        const res = await instance.post(`${prefix}/friend-requests/${friendRequestId}/accept`);
        return res.data;
    }catch(err){
        console.error("서버의 친구 요청 승락 API 호출 실패: ", err);
        throw err;
    }
};

export const declineFriendRequest = async(friendRequestId) => {
    try{
        const res = await instance.delete(`${prefix}/friend-requests/${friendRequestId}`);
        return res.data;
    }catch(err){
        console.error("서버의친구 요청 거절 API 호출 오류: ", err);
        throw err;
    }
};

export const findFriendRequests = async(userId) => {
    try{
        const res = await instance.get(`${prefix}/${userId}/request-friends`)
        console.log(res);
        return res.data;
    }catch(err){
        console.error("서버의 친구 요청 목록 API 호출 오류: ", err);
        throw err;
    }
};
