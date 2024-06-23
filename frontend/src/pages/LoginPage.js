import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'

const LoginPage = () => {
  const [data, setData] = useState({
    email: "",
    password: "",
  })

  const navigate = useNavigate()

  const handleOnChange = (e) => {
    const {name, value} = e.target

    setData((preve) => {
      return{
        ...preve,
        [name] : value
      }
    })
  }

  const handleSubmit = async(e) => {
    e.preventDefault()
    e.stopPropagation()
    
    navigate("/")
  }

  return (
    <div className='container mt-5'>
      <div className='p-4'>
        <div>
          
        </div>
        <h3>환영합니다!</h3>

        <form className='' onSubmit={handleSubmit}>
            <div className='mb-3 col-md-6'>
              <label htmlFor='email' className='form-label fs-5'>이메일</label>
              <input type='text' id='email' name='email' placeholder='a@example.com'
                className='form-control'
                value={data.email}
                onChange={handleOnChange}
                required
              />
            </div>
            <div className='mb-3 col-md-6'>
              <label htmlFor='password' className='form-label fs-5'>비밀번호</label>
              <input type='password' id='password' name='password'
                className='form-control'
                value={data.password}
                onChange={handleOnChange}
                required
              />
            </div>
            <button type='submit' className='btn btn-primary px-4'>로그인</button>
        </form>
        <p className='mt-1'>신규 사용자인가요?<Link to={"/register"} className="icon-link icon-link-hover">회원가입</Link></p>
        <p className='mt-1'>비밀번호를 잊어버리셨나요? <Link to={"/forget-password"} className='icon-link icon-link-hover'>비밀번호 찾기</Link></p>
      </div>
    </div>
  )
}

export default LoginPage