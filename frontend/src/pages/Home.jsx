import React, { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import Sidebar from '../components/Sidebar'
import './css/_layout.css';

const Home = () => {

  // socket connection
  useEffect(() => {
    
  }, []);
  
  return (
    <div className='layout'>
        <Sidebar className='sidebar'/>
        <Outlet className='outlet'/>
    </div>
  )
}

export default Home