import React, { useState } from "react";

const SearchUser = ({onClose}) => {
    const [searchUser, setSearchUser] = useState([]);
    const [loading, setLoading] = useState(false);
    const [search, setSearch] = useState("");



    return (
        <div className="">
            <div>
                {/* 사용자 email 입력 */}
                <div>
                    <input 
                        type="text" placeholder="email입력" 
                        className="" 
                        onChange={(e) => setSearch(e.target.value)} value={search}
                    />
                    <div>
                        찾기버튼?
                    </div>
                </div>

                {/* 찾은 사용자 보여주기 */}
                <div>
                    {
                        // 찾지 못한 경우
                    }

                    {
                        // 로딩중
                    }

                    {
                        // 사용자 찾은 경우
                    }
                </div>
            </div>

            <div className="" onClick={onClose}>
                <img src="" alt="x버튼" />
            </div>
        </div>
    )
}

export default SearchUser;