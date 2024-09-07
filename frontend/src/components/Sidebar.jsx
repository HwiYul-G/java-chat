import {useState} from "react";
import "./css/_sidebar.css";
import {useNavigate} from "react-router-dom";
import Avatar from "./Avatar";
import GroupSidebar from "./sidbar-content/GroupSidebar";
import MyInfoSidebar from "./sidbar-content/MyInfoSidebar";
import ChatListSidebar from "./sidbar-content/ChatListSidebar";
import FriendListSidebar from "./sidbar-content/FriendListSidebar";
import NotificationSidebar from "./sidbar-content/NotificationSidebar";
import {useUser} from "../context/UserContext";

const Sidebar = () => {
    const [activeContent, setActiveContent] = useState(null);
    const {setUserInfo} = useUser();

    const navigate = useNavigate();

    const handleNavClick = (content) => {
        if (activeContent === content)
            setActiveContent(null);
        else
            setActiveContent(content);
    };

    const handleLogoutClicked = () => {
        setActiveContent(null);
        setUserInfo(null);
        localStorage.clear();
        navigate('./login');
    };

    return (
        <div className={`sidebar ${activeContent ? 'expanded' : ''}`}>
            <div className="navigation">
                <ul className="nav">
                    <li className="nav-item mt-xl-auto mb-5" onClick={() => handleNavClick('그룹채팅')}>
                        <i className="bi bi-collection"></i>
                    </li>
                    <li className="nav-item mb-5" onClick={() => handleNavClick('친구목록')}>
                        <i className="bi bi-people-fill"></i>
                    </li>
                    <li className="nav-item mb-5" onClick={() => handleNavClick('채팅내역')}>
                        <i className="bi bi-chat-dots"></i>
                    </li>
                    <li className="nav-item mb-5" onClick={() => handleNavClick('알림')}>
                        <i className="bi bi-bell-fill"></i>
                    </li>
                    <li className="nav-item mb-5" onClick={() => handleNavClick('내정보')}>
                        <Avatar width={35} height={35}/>
                    </li>
                    <li className="nav-item mb-5" onClick={() => handleLogoutClicked()}>
                        <i className="bi bi-box-arrow-left"></i>
                    </li>
                </ul>
            </div>

            {activeContent && (
                <div className="sidebar-content">
                    {activeContent === '그룹채팅' && <GroupSidebar/>}
                    {activeContent === '친구목록' && <FriendListSidebar/>}
                    {activeContent === '채팅내역' && <ChatListSidebar/>}
                    {activeContent === '알림' && <NotificationSidebar/>}
                    {activeContent === '내정보' && <MyInfoSidebar/>}
                </div>
            )}
        </div>
    )
}

export default Sidebar