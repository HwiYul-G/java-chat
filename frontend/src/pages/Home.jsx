import React, { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import Sidebar from '../components/Sidebar'

const Home = () => {

  // socket connection
  useEffect(() => {
    
  }, []);
  
  return (
    <div className='container row'>
      <section className='col-4'>
        <Sidebar/>
      </section>
      {/* message component */}
      <section className='col-8'>
        <Outlet/>
      </section>
    </div>
  )
}

export default Home