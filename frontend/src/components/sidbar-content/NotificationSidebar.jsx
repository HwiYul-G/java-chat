import './css/_common.css';
import AddFriendNotification from './contents/AddFriendNotification';

const NotificationSidebar = () => {
    
    const invitationInfos = [
        {
            invitationId: 1,
            inviterName: "김철수",
            invitationDate: "2024년 7월 20일 04:45 PM"
        },
        {
            invitationId: 2,
            inviterName: "이영희",
            invitationDate: "2024년 7월 20일 05:15 PM"
        },
        {
            invitationId: 3,
            inviterName: "박민수",
            invitationDate: "2024년 7월 20일 06:00 PM"
        }
    ];

    return (
        <div className='d-flex flex-column h-100'>
            <div className='tab-header'>
                <h5>알림</h5>
            </div>

            <div className='hide-scrollbar h-100'>
                <div className='m-4 mt-0'>
                    <div className='notification-list'>
                        {
                            invitationInfos.map(info => (
                                <AddFriendNotification key={info.invitationId} {...info} />
                            ))
                        }
                    </div>
                </div>
            </div>

        </div>
    );
};

export default NotificationSidebar;