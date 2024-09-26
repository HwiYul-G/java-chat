import moment from "moment";

const ChatMessage = ({message, userId}) => {
    const {_id, roomId, senderId, senderName, content, createdAt, type, isDetected, isBadWord} = message;

    if (type === 'DATE') {
        return (
            <div className="divider d-flex align-items-center mb-4">
                <p className="text-center mx-3 mb-0" style={{color: '#a2aab7'}}>{moment(createdAt).format('')}</p>
            </div>
        );
    }

    // 비속어 탐지 결과에 따른 메시지 처리
    let displayContent = content;  // 새로운 변수에 기존 내용을 할당

    if (isDetected) {
        if (isBadWord) {
            displayContent = "비속어가 탐지되어 메시지가 가려졌습니다.";
        }
        // isBadWord가 false인 경우는 displayContent 그대로 사용
    }

    if (userId === senderId) {
        return (
            <div className='d-flex flex-row justify-content-end mb-4'>
                <div>
                    <p className='small p-2 me-3 mb-1 text-white rounded-3 bg-primary'
                       style={{maxWidth: '500px', wordWrap: 'break-word'}}>{displayContent}</p>
                    <p className='small me-3 mb-3 rounded-3 text-muted d-flex justify-content-end'>{moment(createdAt).format('hh:mm')}</p>
                </div>
            </div>
        );
    }
    return (
        <div className='d-flex flex-row justify-content-start'>
            <p className="small mb-0 me-3">{senderName}</p>
            <div>
                <p className="small p-2 mb-1 rounded-3 bg-body-tertiary"
                   style={{maxWidth: '500px', wordWrap: 'break-word'}}>{displayContent}</p>
                <p className="small mb-3 rounded-3 text-muted">{moment(createdAt).format('hh:mm')}</p>
            </div>
        </div>
    );
};

export default ChatMessage;