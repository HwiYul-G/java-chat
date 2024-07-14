import './App.css';
import { Outlet } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'

function App() {
  return (
      <main>
        <Outlet/>
      </main>
  );
}

export default App;
