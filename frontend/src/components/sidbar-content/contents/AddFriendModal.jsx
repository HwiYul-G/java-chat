import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useState } from 'react';

const AddFriendModal = ({show, onClose}) => {
    const [email, setEmail] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleInviteClicked = () => {

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