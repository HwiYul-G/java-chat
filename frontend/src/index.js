import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { RouterProvider } from 'react-router-dom';
import router from './routes';
import { UserProvider } from './context/UserContext';
import { WebSocketProvider } from './context/WsContext';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
    <UserProvider>
      <WebSocketProvider>
        <RouterProvider router={router}>
          <App />
        </RouterProvider>
      </WebSocketProvider>
    </UserProvider>
  // </React.StrictMode>
);
