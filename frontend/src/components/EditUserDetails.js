import React, { useEffect, useState } from "react";
import Avatar from "./Avatar";
import {updateUserInfo} from "../api/userApi";
import { useUser } from "../context/UserContext";

const EditUserDetails = ({onClose, userInfo}) => {
    const {setUserInfo} = useUser();

    const [data, setData] = useState({
        'email': userInfo.email,
        'username': userInfo.username,
        'enabled':userInfo.enabled,
        'roles':userInfo.roles,
    });

    useEffect(() => {
        setData((prev) => {
            return{
                ...prev,
                ...userInfo,
            };
        });
    }, [userInfo]);

    const handleOnChange = (e) => {
        const {name, value} = e.target;
        setData((preve) => {
            return ({
                ...preve,
                [name]: value,
            });
        });
    }

    const handleOpenUploadPhoto = (e) => {
        e.preventDefault();
        e.stopPropagation();
    }

    const handlePhotoChange = (e) => {
        const file = e.target.files[0];
        if(file){
            // TODO: server에서 image 처리도 추가한 후 진행
        }
    };

    const handleSubmit = async(e) => {
        e.preventDefault();
        e.stopPropagation();
        try{
            const res = await updateUserInfo(userInfo.id, data);
            if(res.flag){
                setUserInfo(data);
                onClose();
            }
        } catch(err){
            console.log(err);
        }
    }

    return (
        <div className="" >
            <div className="">
                <h2 className="">프로필</h2>
                <p> 
                    {
                        `이메일 : ${userInfo.email}
                        이름: ${userInfo.username}`
                    }
                </p>
                <form>
                    <div>
                        <label htmlFor="username">새 이름 : </label>
                        <input
                            type="text"
                            name="username"
                            id="username"
                            value={data.username}
                            onChange={handleOnChange}
                        />
                    </div>
                    <div>
                        <div className="">새 프로필 이미지</div>
                        <div className="d-flex align-items-center form-control">
                            <Avatar width={40} height={40}/>
                            <label htmlFor="profile_pic">
                                <button className="border-0 bg-transparent" onClick={handleOpenUploadPhoto}>사진 변경</button>
                                <input
                                    type="file"
                                    id="profile_pic"
                                    className=""
                                    onChange={handlePhotoChange}
                                    hidden
                                />
                            </label>
                        </div>
                    </div>
                    <div className="d-flex justify-content-end p-2">
                        <button onClick={onClose} className="btn btn-secondary m-1">취소</button>
                        <button onClick={handleSubmit} className="btn btn-primary m-1">완료</button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default EditUserDetails;