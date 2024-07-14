import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import Avatar from './Avatar';
import Message from './Message';

const MessagePage = () => {
  const params = useParams();  // console.log("params : ", params.userId);

  const [allMessage, setAllMessag] = useState([]);

  const [message, setMessage] = useState({
    text: "",
    imageUrl: "",
    videoUrl: "",
  });

  useEffect(() => {

  }, [allMessage]);

  useEffect(() => {
    // socket connection 관련 처리
  }, []);

  const handleOnChange = (e) => {
    const {name, value} = e.target;
    setMessage(prev => {
      return {
        ...prev,
        text: value
      };
    });
  }

  const handleSendMessage = (e) => {
    e.preventDefault();

  }


  return (
    <div className='bg-secondary'>
      <header>
        <Avatar width={50} height={50} name={params.username} userId={params.userId}/>
      </header>

      {/* show all message */}
      <section>
        {/* all message show here */}
        <div className=''>
          {
            allMessage.map((msg) => <Message message={msg}/>)
          }
        </div>
        {/* upload image display */}
        {/* upload video display */}

      </section>

      {/* send message */}
      <section>
        <div className='relative'>

        </div>

        <form>
          <input
            type='text'
            placeholder='메시지를 입력하세요.'
            className='py-1 px-4'
            value={message.text}
            onChange={handleOnChange}
          />
          <button className='btn btn-primary' onClick={handleSendMessage}>전송</button>
        </form>
        
      </section>
    </div>
  )
}

export default MessagePage