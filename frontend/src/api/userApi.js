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