import React from 'react'
import logo from '../assets/chat_log_icon.png'

const AuthLayouts = ({children}) => {
  return (
    <div>
      <header className='text-center dy-3'>  
          <img
              src={logo}
              alt='log'
              width={60}
              height={60}
          />
          Chat
      </header>
      <main>
        {children}
      </main>
    </div>
  )
}


export default AuthLayouts
