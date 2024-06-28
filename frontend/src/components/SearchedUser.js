import React from "react";
import Avatar from "./Avatar";

const SearchedUser = ({user, onClose}) => {
    return (
        <Link onClose={onClose}>
            <div>
                <Avatar
                    width={50} height={50}
                />
            </div>
            <div>
                <div className="">
                    {user?.name}
                </div>
                <p className="">{user?.email}</p>
            </div>
        </Link>
    )
}

export default SearchedUser;