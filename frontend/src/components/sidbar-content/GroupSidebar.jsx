const GroupSidebar = () => {
    return (
        <div className="tab-pane">
            <div className="d-flex flex-column h-100">
                <div className="tab-header">

                </div>


                <div className="">
                    <ul>
                        <li>
                            만들기
                        </li>
                        <li>
                            참여하기
                        </li>
                    </ul>
                </div>

                <div className="">
                    <div className="tab-pane">
                        만들기 관련 내용
                    </div>  
                    <div className="tab-pane">
                        참여하기 관련 내용
                    </div>
                </div>

            </div>
        </div>
    );
};

export default GroupSidebar;