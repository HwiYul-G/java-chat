import Avatar from '../../Avatar.jsx';

const Friend = ({id, name, email, imgUrl}) => {
    return (
        <div className="card-body">
            <div className="d-flex align-items-center">
                <Avatar width={40} height={40} imageUrl={imgUrl}/>
                <div className="flex-grow-1 overflow-hidden">
                    <div className="d-flex algin-items-center mb-1">
                        <h6 className="text-truncate mb-0 me-auto">{name}</h6>
                    </div>
                    <div className="d-flex algin-items-center">
                        <div className="text-truncate me-auto">{email}</div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Friend;