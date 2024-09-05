import instance from ".";

const prefix = "/notifications";

export const inviteFriend = async(userId, friendEmail) => {
    try{
        const requestData =  {"userId" : userId, "friendEmail" : friendEmail}
        const res = instance.post(`${prefix}/friend-invitation`, requestData);
        console.log(res.data);
        return res.data;
    } catch (err){
        console.error(err);
        throw err;
    }
};

export const updateNotificationStatus = async (notificationId, isAccept) => {
    try{
        const res = await instance.put(`${prefix}/${notificationId}`, isAccept);
        console.log(res.data);
        return res.data;
    } catch(err){
        console.error(err);
        throw err;
    }
};
