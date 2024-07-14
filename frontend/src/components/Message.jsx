const Message = (message) => {
    return (
    <div>
        <div>
            {/* img or video */}
        </div>
        <p className="px-2">{message.text}</p>
        <p className="">{/*moment(message.createdAt).format('hh:mm')*/}</p>
    </div>
    );
};

export default Message;