import React, { useState } from 'react'
import { Link } from 'react-router-dom';

const Forgotpassword = () => {
  const [email, setEmail] = useState("");
  
  const handleOnChange = (e) => {
    
  }

  const handleSubmit = () => {

  }

  return (
    <div className='container mt-5'>
      <div className='p-4'>

        <form className='' onSubmit={handleSubmit}>
            <div className='mb-3 col-md-6'>
              <label htmlFor='email' className='form-label fs-5'>이메일</label>
              <input type='text' id='email' name='email' placeholder='a@example.com'
                className='form-control'
                value={email}
                onChange={handleOnChange}
                required
              />
            </div>
            <button type='submit' className='btn btn-primary px-4'>임시 비밀번호 발송</button>
        </form>
        <p className='mt-1'>로그인 페이지로 바로가기: <Link to={"/login"} className="icon-link icon-link-hover">로그인</Link></p>
      </div>
    </div>
  )
}

export default Forgotpassword;