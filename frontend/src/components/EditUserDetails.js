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
        <div className="">
            <div className="">
                <h2 className="">ProfileDetails</h2>
                <p>Edit user details</p>

                <form>
                    <div>
                        <label htmlFor="name">이름</label>
                        <input
                            type="text"
                            name="name"
                            id="name"
                            
                            onChange={handleOnChange}
                        />
                    </div>
                    <div>
                        <div className="">사진</div>
                        <div>
                            <Avatar width={40} height={40}/>
                            <label htmlFor="profile_pic">
                                <button className="" onClick={handleOpenUploadPhoto}>사진 변경</button>
                                <input
                                    type="file"
                                    id="profile_pic"
                                    className="hidden"
                                    onChange={handleUploadPhoto}
                                />
                            </label>
                        </div>
                    </div>
                    <div className="">
                        <button onClick={onClose} className="btn btn-primary">취소</button>
                        <button onClick={handleSubmit} className="btn btn-primary">완료</button>
                    </div>
                </form>



            </div>
        </div>
    )
}

export default EditUserDetails;