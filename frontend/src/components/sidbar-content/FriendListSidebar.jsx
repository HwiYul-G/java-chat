import './css/_common.css';
import './contents/Friend';
import Friend from './contents/Friend';

const FriendListSidebar = () => {

    const friends = [
        { id: 1, name: 'Alice', email: 'alice@example.com', imgUrl: '' },
        { id: 2, name: 'Bob', email: 'bob@example.com', imgUrl: '' },
        { id: 3, name: 'Charlie', email: 'charlie@example.com', imgUrl: '' },
    ];

    return (
        <div className='tab-pane'>
            <div className='d-flex flex-column h-100'>
                <div className='tab-header'>
                    <h5>친구 목록</h5>
                </div>

                <div className='hide-scrollbar h-100'>
                    <div className='m-4'>
                        <ul className='list-unstyled'>
                            { friends.map(friend => (
                                <li className='card contact-item' key={friend.id}>
                                    <Friend {...friend}/>
                                </li>
                            ))
                            }
                        </ul>
                    </div>
                </div>

                <div className='tab-footer border-top p-4'>
                    <button className='btn btn-sm btn-primary w-100'>친구 추가</button>
                </div>
            </div>
        </div>
    );
};

export default FriendListSidebar;