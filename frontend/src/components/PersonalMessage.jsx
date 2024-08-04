import moment from "moment";

const PersonalMessage = ({message, userId}) => {
    const {id, senderId, roomId, content, createdAt, type } = message;

    if(type === 'DATE'){
        return (
            <div class="divider d-flex align-items-center mb-4">
              <p class="text-center mx-3 mb-0" style={{color: '#a2aab7'}}>{moment(createdAt).format('')}</p>
            </div>
        );
    }

    if(userId === senderId){
        return (
            <div className='d-flex flex-row justify-content-end mb-4'>
                <div>
                    <p className='small p-2 me-3 mb-1 text-white rounded-3 bg-primary' style={{maxWidth:'500px', wordWrap:'break-word'}}>{content}</p>
                    <p className='small me-3 mb-3 rounded-3 text-muted d-flex justify-content-end'>{moment(createdAt).format('hh:mm')}</p>
                </div>
            </div>
        );
    }
    return (
        <div className='d-flex flex-row justify-content-start'>
            <p className="small p-2 mb-1 rounded-3 bg-body-tertiary" style={{maxWidth:'500px', wordWrap:'break-word'}}>{content}</p>
            <p className="small mb-3 rounded-3 text-muted">{moment(createdAt).format('hh:mm')}</p>
        </div>
    );
};

export default PersonalMessage;