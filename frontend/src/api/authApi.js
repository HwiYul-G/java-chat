import instance from ".";
import qs from "qs";

const prefix = '/auth';

export const login = async ({email, password}) => {
    const data = {username: email, password: password};
    try{
        const res = await instance.post(`${prefix}/login`, qs.stringify(data), {
            auth: data,
        });
        return res.data;
    } catch(err){
        console.error("서버의 로그인 API 호출 실패: ", err);
        throw err;
    }
};
