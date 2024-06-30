import React, { useState } from "react";
import Loading from "./Loading";

const SearchUser = ({onClose}) => {
    const [searchUser, setSearchUser] = useState([]);
    const [loading, setLoading] = useState(false);
    const [search, setSearch] = useState("");

    const handleSearchUser = async () => {

    }

    return (
        <div className="">
            <div>
                {/* 사용자 email 입력 */}
                <div className="">
                    <input 
                        type="text" 
                        placeholder="email입력" 
                        className="" 
                        onChange={(e) => setSearch(e.target.value)} 
                        value={search}
                    />
                    <div>
                        <button className="btn btn-primary">찾기</button>
                    </div>
                </div>

                {/* 찾은 사용자 보여주기 */}
                <div>
                    {
                        // 찾지 못한 경우
                    }

                    {
                        loading && (
                            <p><Loading/></p>
                        )
                    }

                    {
                        // 사용자 찾은 경우
                    }
                </div>
            </div>

            <button onClick={onClose} className="btn btn-secondary">취소</button>
        </div>
    )
}

export default SearchUser;