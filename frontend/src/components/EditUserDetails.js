import React, { useState } from "react";
import Avatar from "./Avatar";

const EditUserDetails = ({onClose, user}) => {

    const handleOnChange = (e) => {
        
    }

    const handleOpenUploadPhoto = (e) => {

    }

    const handleUploadPhoto = async(e) => {

    }

    const handleSubmit = async(e) => {
        
    }

    return (
        <div className="" >
            <div className="">
                <h2 className="">프로필</h2>
                <p> email 주소 </p>
                <form>
                    <div>
                        <label htmlFor="name">이름 : </label>
                        <input
                            type="text"
                            name="name"
                            id="name"
                            onChange={handleOnChange}
                        />
                    </div>
                    <div>
                        <div className="">프로필 이미지</div>
                        <div className="d-flex align-items-center form-control">
                            <Avatar width={40} height={40}/>
                            <label htmlFor="profile_pic">
                                <button className="border-0 bg-transparent" onClick={handleOpenUploadPhoto}>사진 변경</button>
                                <input
                                    type="file"
                                    id="profile_pic"
                                    className=""
                                    onChange={handleUploadPhoto}
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