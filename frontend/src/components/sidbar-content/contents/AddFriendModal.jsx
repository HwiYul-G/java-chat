import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useState } from 'react';
import { useUser } from '../../../context/UserContext';
import { inviteFriend } from '../../../api/userApi';

const AddFriendModal = ({show, onClose}) => {
    const {userInfo} = useUser(); // userInfo.id가 userId이다. 

    const [email, setEmail] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleInviteClicked = async() => {
        if(userInfo.email === email){
            setErrorMessage("자기 자신을 친구로 초대할 수 없습니다.");
            return;
        }
        try{
            const res = await inviteFriend({userId: userInfo.id, friendEmail: email});
            console.log(res);
            if(res.flag){
                setEmail('');
                onClose();
            } else {
                setErrorMessage(res.message);
            }
        }catch(err){
            console.error(err);
            setErrorMessage(err.message || '친구 초대 중 오류가 발생했습니다.');
        }
    };

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Dialog> 
                <Modal.Header closeButton> 
                    <Modal.Title> 친구 추가</Modal.Title> 
                </Modal.Header> 
                <Modal.Body>
                    <div className='d-flex flex-column'>
                        <span>초대할 친구의 이메일을 입력하세요.</span>
                        <input
                            type='text'
                            placeholder='example@example.com'
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        { errorMessage !== '' && <p>{errorMessage}</p> }
                    </div>
                </Modal.Body> 
                <Modal.Footer> 
                    <Button variant="primary" onClick={handleInviteClicked}> 초대 </Button> 
                    <Button variant="secondary" onClick={onClose}> 닫기 </Button> 
                </Modal.Footer> 
            </Modal.Dialog> 
        </Modal> 
    );
};

export default AddFriendModal;