import { useState } from "react";
import { acceptFriendRequest, declineFriendRequest } from "../../../api/userApi";

const AddFriendNotification = ({invitationId, inviterName, invitationDate}) => {
    const [isResponded, setIsResponded] = useState(false);

    const handleReject = async () => {
        const res = await declineFriendRequest(invitationId);
        if(res.flag){
            setIsResponded(true);
        }
    };

    const handleAccept = async () => {
        const res = await acceptFriendRequest(invitationId);
        if(res.flag){
            setIsResponded(true);
        }
    };

    return (
        <div className='card mb-3'>
            <div className='card-body'>
                <div className='d-flex align-items-center'>
                    <div className='flex-grow-1'>
                        <div className='d-flex align-items-center overflow-hidden'>
                            <h5 className='me-auto text-break mb-0'>{inviterName}</h5>
                            <span className='small text-muted text-nowrap ms-2'>{invitationDate}</span>
                        </div>
                        <div className='d-flex align-items-center'>
                            <div className='line-clamp me-auto'>당신에게 친구 초대를 요청합니다.</div>
                        </div>
                    </div>
                </div>
            </div>
            {!isResponded && (
                <div className='card-footer'>
                    <div className='row gx-4'>
                        <div className='col'>
                            <button className='btn btn-secondary btn-sm w-100' onClick={handleReject}>거절</button>
                        </div>
                        <div className='col'>
                            <button className='btn btn-primary btn-sm w-100' onClick={handleAccept}>수락</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AddFriendNotification;