import React, { useState } from "react";
import SearchedUser from "./SearchedUser";
import {findUserByEmail} from "../api/userApi";

const SearchUser = ({onClose}) => {
    const [searchedUser, setSearchedUser] = useState();
    const [errMessage , setErrMessage] = useState('');
    const [errStatus, setErrStatus] = useState();
    const [searchedEmail, setSearchedEmail] = useState("");

    const handleSearchUser = async () => {
        findUserByEmail(searchedEmail)
            .then(searchedUser => {
                setSearchedUser(searchedUser.data);
                setErrMessage('');
                console.log(searchedUser);
            })
            .catch(err => {
                console.log("사용자 검색 실패: ", err)
                setErrMessage(err.message);
                setErrStatus(err.response.status);
            });
    }

    return (
        <div className="">
            <div>
                {/* 사용자 email 입력 후 검색 버튼 클릭 부분 */}
                <div className="">
                    <label htmlFor="searchedEmail">검색할 이메일 : </label>
                    <input 
                        type="text"
                        id="searchedEmail"
                        placeholder="email입력" 
                        className="" 
                        onChange={(e) => setSearchedEmail(e.target.value)} 
                        value={searchedEmail}
                    />
                    <div>
                        <button className="btn btn-primary" onClick={handleSearchUser}>찾기</button>
                    </div>
                </div>
                <button onClick={onClose} className="btn btn-secondary">취소</button>

                {/* 찾은 사용자를 보여주는 부분 */}
                <div>
                    {
                        !searchedUser && errStatus === 404  &&(
                            <p>해당 이메일의 사용자가 존재하지 않습니다.</p>
                        )
                    }
                    {
                        searchedUser  &&(
                            <SearchedUser user={searchedUser} onClose={onClose}/>
                        )
                    }
                </div>
            </div>
            {errMessage!='' && errStatus !== 404 &&<p> {errMessage} </p>}
        </div>
    )
}

export default SearchUser;