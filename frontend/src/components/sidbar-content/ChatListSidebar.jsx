import { useState } from "react";
import './css/_common.css'
import PersonalChatList from "./contents/PersonalChatList";
import GroupChatList from "./contents/GroupChatList";

const ChatListSidebar = () => {
    const [currentTab, setCurrentTab] = useState(0);

    const tabArr = [
        {name: "개인", content: <PersonalChatList/>},
        {name: "그룹", content: <GroupChatList/>}
    ];

    return (
        <div className="tab=pane">
            <div className="d-flex flex-column h-100">
                <div className="tab-header">
                    <h5>채팅</h5>
                </div>

                <div className="text-center">
                    <ul className="tab-list">
                        {tabArr.map((tab, index) => (
                            <li key={index} className={`tab-item ${currentTab === index ? 'active' : ''}`}
                                onClick={() => setCurrentTab(index)}>
                                {tab.name}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="hide-scrollbar h-100">
                    <div className="tab-content">
                        {tabArr[currentTab].content}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ChatListSidebar;