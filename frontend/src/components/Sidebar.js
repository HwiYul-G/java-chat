import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Avatar from './Avatar';
import EditUserDetails from './EditUserDetails';
import SearchUser from './SearchUser';
import { NavLink, useNavigate } from 'react-router-dom';


const Sidebar = () => {
  const user = null

  const [editUserOpen, setEditUserOpen] = useState(false)
  const [allUser, setAllUser] = useState([])
  const [openSearchUser, setOpenSearchUser] = useState(false)

  const navigate = useNavigate()

  const handleLogout = () => {

  }

  return (
    <div className='row vh-100'>
      <div className='col-4 d-flex flex-column bg-light justify-content-between'>
        <div className='mb-3'>
          <NavLink className='p-3 justify-content-center'>
            <img src={require('../assets/messenger.png')} alt='메신저' width={40} height={40}/>
          </NavLink>
          <div className='p-3 justify-content-center' onClick={()=>setOpenSearchUser(true)}>
            <img src={require('../assets/add-friend.png')} alt='친구추가' width={40} height={40}/>
          </div>
        </div>

        <div className='mb-3'>
          <div className='p-3 justify-content-center' onClick={()=>setEditUserOpen(true)}>
            <Avatar width={40} height={40}/>
          </div>
          <div className='p-3 justify-content-center' onClick={handleLogout}>
            <img src={require('../assets/logout.png')} alt='로그아웃' width={40} height={40}/>
          </div>
        </div>
      </div>

      <div className='col-8 bg-primary'>
        <div className='justify-content-center'>
          <h2>메시지</h2>
        </div>
        
        <div data-bs-spy="scroll" className=''>
          {
            allUser.length === 0 && (
              <div className='mt-12'>
                <p className=''>친구를 추가해 보세요.</p>
              </div>
            )
          }
          {
            allUser.map(() => {
              return (
                <NavLink>
                  <div>
                    <Avatar  width={40} height={40}/>
                  </div>
                  <div>
                    <h3>이름</h3>
                    <div>
                      <div>
                        {

                        }
                        {

                        }
                      </div>
                      <p>마지막메시지</p>
                    </div>
                  </div>
                  {

                  }
                </NavLink>
              )
            })
          }
        </div>
      </div>
      {
        editUserOpen && (
          <EditUserDetails/>
        )
      }
      {
        openSearchUser && (
          <SearchUser/>
        )
      }
    </div>
  )
}

export default Sidebar