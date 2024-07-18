import { useState } from 'react';
import './css/_common.css';
import MakeGroupChatRoom from './contents/MakeGroupChatRoom';
import EnterGroupChatRoom from './contents/EnterGroupChatRoom';

const GroupSidebar = () => {
    const [currentTab, setCurrentTab] = useState(0);

    const tabArr = [
        { name: "만들기", content: <MakeGroupChatRoom/> },
        { name: "참여하기", content: <EnterGroupChatRoom/> }
    ];

    return (
        <div className="tab-pane">
            <div className="d-flex flex-column h-100">
                <div className="tab-header">
                    <h5>그룹 채팅 만들기 및 참여</h5>
                </div>

                <div className="text-center mb-4">
                    <ul className="tab-list">
                        {tabArr.map((tab, index) => (
                            <li 
                                key={index} 
                                className={`tab-item ${currentTab === index ? 'active' : ''}`}
                                onClick={() => setCurrentTab(index)}
                            >
                                {tab.name}
                            </li>
                        ))}
                    </ul>
                </div>
                
                <div className="hide-scrollbar h-100">
                    <div className="tab-content m-4 mt-0">
                        {tabArr[currentTab].content}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default GroupSidebar;