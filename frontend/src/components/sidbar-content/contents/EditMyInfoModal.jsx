import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';
import Avatar from '../../Avatar';
import { useUser } from '../../../context/UserContext';
import { updateUserInfo } from '../../../api/userApi';

const EditMyInfoModal = ({show, onClose}) => {
    const {userInfo, setUserInfo} = useUser();
    const [data, setData] = useState({
       'email': userInfo.email,
       'username': userInfo.username,
       'enabled': userInfo.enabled,
       'roles': userInfo.roles 
    });

    useEffect(() => {
        setData((prev) => {
            return {
                ...prev,
                ...userInfo,
            };
        })
    }, [userInfo]);
    
    const handleOnChange = (e) => {
        const {name, value} = e.target;
        setData((prev) => {
            return({
                ...prev,
                [name]: value,
            });
        });
    };

    const handleOpenUploadPhoto = (e) => {
        e.preventDefault();
        e.stopPropagation();
    }

    const handlePhotoChange = (e) => {
        const file = e.target.files[0];
        if(file){
            // TODO: server에서 img 처리도 추가한 후 진행
        }
    };

    const handleSubmit = async() => {
        try{
            const res = await updateUserInfo(userInfo.id, data);
            if(res.flag){
                setUserInfo(data);
                onClose();
            }
        } catch(err){
            console.error(err);
        }
    };


    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Dialog>
                <Modal.Header closeButton>
                    <Modal.Title>내 정보 수정</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form>
                        <div>
                            <label htmlFor='username'>새 이름 : </label>
                            <input 
                                type='text'
                                name='username'
                                id='username'
                                value={data.username}
                                onChange={handleOnChange}
                            />
                        </div>

                        <div>
                            <div className=''>새 프로필 이미지</div>
                            <div className='d-flex align-items-center form-control'>
                                <Avatar width={40} height={40}/>
                                <label htmlFor='profile_pic'>
                                    <button className='border-0 bg-transparent' onClick={handleOpenUploadPhoto}>사진 변경</button>
                                    <input
                                        type='file'
                                        id='profile_pic'
                                        className=''
                                        onChange={handlePhotoChange}
                                        hidden
                                    />
                                </label>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer> 
                    <Button variant="primary" onClick={handleSubmit}> 완료 </Button> 
                    <Button variant="secondary" onClick={onClose}> 닫기 </Button> 
                </Modal.Footer> 
            </Modal.Dialog>
        </Modal>
    );
};

export default EditMyInfoModal;