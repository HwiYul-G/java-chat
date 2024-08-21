import './css/_common.css';
import AddFriendNotification from './contents/AddFriendNotification';
import { useEffect, useState } from 'react';
import { useUser } from '../../context/UserContext';
import { findFriendRequests } from '../../api/userApi';
import moment from 'moment';

const NotificationSidebar = () => {
    const { userInfo } = useUser();

    const [invitationInfos, setInvitationInfos] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchFriendRequests = async () => {
            try{
                const data = await findFriendRequests(userInfo.id);
                const invitationInfoData = data.data.map(info => ({
                    invitationId: info.id,
                    inviterName: info.senderName,
                    invitationDate: moment(info.createdAt).format('')
                }));
                setInvitationInfos(invitationInfoData);
            }catch(err){
                setError("친구 요청 정보를 가져오는 데 실패했습니다.");
            }
        };

        fetchFriendRequests();
    }, []);

    return (
        <div className='d-flex flex-column h-100'>
            <div className='tab-header'>
                <h5>알림</h5>
            </div>

            <div className='hide-scrollbar h-100'>
                <div className='m-4 mt-0'>
                    {error && <p className="error-message">{error}</p>}
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