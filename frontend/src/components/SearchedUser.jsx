import React from "react";
import Avatar from "./Avatar";
import {Link, useNavigate} from 'react-router-dom';

const SearchedUser = ({user, onClose}) => {
    const navigate = useNavigate();

    const handleAddFriend = () => {
        navigate(`/${user.id}`);
        onClose();
    };

    return (
        <div className="d-flex align-items-center border border-light align-middle">
            <Avatar width={50} height={50}/>
            <div className="m-1 d-flex flex-column">
                <div>이름: {user?.username}</div>
                <p>이메일: {user?.email}</p>
            </div>
            <button className="btn btn-primary" onClick={handleAddFriend}>채팅</button>
        </div>
    )
}

export default SearchedUser;