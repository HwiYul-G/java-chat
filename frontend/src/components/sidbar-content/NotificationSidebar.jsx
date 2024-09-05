import './css/_common.css';
import Notification from './contents/Notification';
import { useEffect, useState } from 'react';
import { useUser } from '../../context/UserContext';
import {getNotifications} from "../../api/userApi";

const NotificationSidebar = () => {
    const { userInfo } = useUser();
    const [notifications, setNotifications] = useState([]);
    const [error, setError] = useState('');

    const fetchNotifications = async () => {
        try{
            const data = await getNotifications(userInfo.id);
            if(data.flag){
                setNotifications(data.data);
            }
        }catch(err){
            setError(err.message);
        }
    };

    useEffect(() => {
        fetchNotifications();
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
                            notifications.map(info => (
                                <Notification key={info.id} {...info}/>
                            ))
                        }
                    </div>
                </div>
            </div>

        </div>
    );
};

export default NotificationSidebar;