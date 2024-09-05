import { useState } from 'react';
import Avatar from '../Avatar';
import './css/_common.css';
import { useUser } from '../../context/UserContext';
import EditMyInfoModal from "./contents/EditMyInfoModal";

const MyInfoSidebar = () => {
    const [showModal, setShowModal] = useState(false);
    const { userInfo } = useUser();

    const handleShowModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <div className='d-flex flex-column h-100'>
            <div className='tab-header'>
                <h5>내 정보</h5>
            </div>

            <div className='d-flex flex-column align-items-center'>
                <Avatar width={48} height={48}/> 
                <h5 className='m-1'>{userInfo.username}</h5>
            </div>

            <div className='hide-scrollbar h-100'>
                <ul className='list-group list-group-flush'>
                    <li className='list-group-item p-4'>
                        <div className='col'>
                            <h5 className='mb-1'>이메일</h5>
                            <p className='text-muted mb-0'>{userInfo.email}</p>
                        </div>
                    </li>
                </ul>
            </div>


            <div className='tab-footer border-top p-4'>
                <button className='btn btn-sm btn-primary w-100' onClick={handleShowModal}>내 정보 수정</button>
            </div>

            <EditMyInfoModal show={showModal} onClose={handleCloseModal}/>
        </div>
    );
};

export default MyInfoSidebar;