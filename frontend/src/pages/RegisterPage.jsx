import React, {useState} from 'react'
import {IoClose} from 'react-icons/io5'
import {Link, useNavigate} from 'react-router-dom'
import {register} from '../api/userApi';

const RegisterPage = () => {
    // TODO: 사용자 이미지 추가 필요
    const [data, setData] = useState({
        email: "",
        username: "",
        password: "",
        roles: "user",
    });

    const [uploadPhoto, setUploadPhoto] = useState("")

    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    const handleOnChange = (e) => {
        const {name, value} = e.target

        setData((prev) => {
            return {
                ...prev,
                [name]: value
            }
        })
    }

    const handleUploadPhoto = (e) => {
        const file = e.target.files[0]

        setUploadPhoto(file)
    }

    const handleClearUploadPhoto = (e) => {
        e.stopPropagation()
        e.preventDefault()
        setUploadPhoto(null)
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        e.stopPropagation()

        await register(data)
            .then((res) => {
                if (res.flag) {
                    navigate('/login');
                }
            }).catch((err) => {
                setMessage(err.message);
            });
    }


    return (
        <div className='container mt-5'>
            <div className='p-4'>
                <h3>Chat app에 오신 걸 환영합니다.</h3>
                <form className='' onSubmit={handleSubmit}>
                    <div className='mb-3 col-md-6'>
                        <label htmlFor='username' className='from-label fs-5'>이름</label>
                        <input type='text' id='username' name='username' placeholder='your name'
                               className='form-control'
                               value={data.username}
                               onChange={handleOnChange}
                               required
                        />
                    </div>

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

                    <div className='mb-3 col-md-6'>
                        <label htmlFor='profile_pic' className='form-label fs-5'>사진
                            <div className='d-flex align-items-center form-control rounded'>
                                <p className='fs-6 mb-0 flex-grow-1'>
                                    {
                                        uploadPhoto?.name ? uploadPhoto?.name : "프로필 사진 업로드"
                                    }
                                </p>
                                {
                                    uploadPhoto?.name && (
                                        <button className='ml-2 border-0 bg-transparent' onClick={handleClearUploadPhoto}>
                                            <IoClose/>
                                        </button>
                                    )
                                }
                            </div>
                        </label>
                        <input
                            type='file'
                            id='profile_pic'
                            name='profile_pic'
                            hidden
                            onChange={handleUploadPhoto}
                        />
                    </div>
                    <button type='submit' className='btn btn-primary px-4'> 회원가입 완료</button>
                </form>
                {message !== '' && <p> {message} </p>}
                <p className='mt-1'>이미 아이디가 존재하나요? <Link to={"/login"}
                                                         className="icon-link icon-link-hover">Login</Link></p>
            </div>
        </div>
    )
}

export default RegisterPage