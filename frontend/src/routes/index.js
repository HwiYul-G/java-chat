import { createBrowserRouter } from "react-router-dom";
import RegisterPage from "../pages/RegisterPage";
import LoginPage from "../pages/LoginPage";
import Home from "../pages/Home";
import MessagePage from "../components/MessagePage";
import App from "../App";
import AuthLayouts from "../layout";
import Forgotpassword from "../pages/Forgotpassword";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        children: [
            {
                path: 'register',
                element: <AuthLayouts><RegisterPage/></AuthLayouts>
            },
            {
                path: 'login',
                element: <AuthLayouts><LoginPage/></AuthLayouts>
            },
            {
                path: 'forget-password',
                element: <AuthLayouts><Forgotpassword/></AuthLayouts>
            },
            {
                path: '',
                element: <Home/>,
                children: [
                    {
                        path: ':roomId',
                        element: <MessagePage/>
                    }
                ]
            }
        ]
    }
])

export default router