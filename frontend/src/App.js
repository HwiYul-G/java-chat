import './App.css';
import { Outlet } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css';

function App() {
  return (
      <main>
        <Outlet/>
      </main>
  );
}

export default App;
