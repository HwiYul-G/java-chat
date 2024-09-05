import {useState} from "react";
import moment from "moment";
import {updateNotificationStatus} from "../../../api/notificationApi";

const Notification = ({id, content, isRead, createdAt, status, type}) => {
    const [isResponded, setIsResponded] = useState(status === 'ACCEPTED' || status === 'REJECTED');
    const formattedDate = moment(createdAt).format("YYYY-MM-DD HH:mm");
    const [errorMessage, setErrorMessage] = useState("");

    const handleResponse = async (response) => {
        try {
            const res = await updateNotificationStatus(id, response);
            if (res.flag) {
                setIsResponded(true);
            }
        } catch (err) {
            setErrorMessage(err.message);
        }
    }

    const shouldShowActions = status === 'PENDING' && type === 'FRIEND_REQUEST';

    return (
        <div className='card mb-3'>
            <div className='card-body'>
                <div className='d-flex align-items-center'>
                    <div className='flex-grow-1'>
                        <div className='d-flex align-items-center overflow-hidden'>
                            <h5 className='me-auto text-break mb-0'>{content}</h5>
                            <span className='small text-muted text-nowrap ms-2'>{formattedDate}</span>
                        </div>
                        <div className='d-flex align-items-center'>
                            <div className='line-clamp me-auto'>{content}</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* 수락/거절 버튼이 보여질 조건 */}
            {!isResponded && shouldShowActions && (
                <div className='card-footer'>
                    <div className='row gx-4'>
                        <div className='col'>
                            <button
                                className='btn btn-secondary btn-sm w-100'
                                onClick={() => handleResponse(false)}> {/* 거절 처리 */}
                                거절
                            </button>
                        </div>
                        <div className='col'>
                            <button
                                className='btn btn-primary btn-sm w-100'
                                onClick={() => handleResponse(true)}> {/* 수락 처리 */}
                                수락
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* 에러 메시지를 표시할 영역 */}
            {errorMessage && (
                <div className="alert alert-danger mt-2" role="alert">
                    {errorMessage}
                </div>
            )}
        </div>
    );
};

export default Notification;