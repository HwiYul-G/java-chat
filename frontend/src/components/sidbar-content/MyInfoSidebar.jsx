import Avatar from '../Avatar';
import './css/_common.css';

const MyInfoSidebar = () => {
    return (
        <div className='d-flex flex-column h-100'>
            <div className='tab-header'>
                <h5>내 정보</h5>
            </div>

            <div className='d-flex flex-column align-items-center'>
                <Avatar width={48} height={48}/> 
                <h5 className='m-1'>로그인한 사용자 이름</h5>
            </div>

            <div className='hide-scrollbar h-100'>
                <ul className='list-group list-group-flush'>
                    <li className='list-group-item p-4'>
                        <div className='col'>
                            <h5 className='mb-1'>이메일</h5>
                            <p className='text-muted mb-0'>test@google.com</p>
                        </div>
                    </li>
                </ul>
            </div>


            <div className='tab-footer border-top p-4'>
                    <button className='btn btn-sm btn-primary w-100'>내 정보 수정</button>
            </div>
        </div>
    );
};

export default MyInfoSidebar;