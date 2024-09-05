import { createBrowserRouter } from "react-router-dom";
import RegisterPage from "../pages/RegisterPage";
import LoginPage from "../pages/LoginPage";
import Home from "../pages/Home";
import App from "../App";
import AuthLayouts from "../layout";
import Forgotpassword from "../pages/Forgotpassword";
import MessagePage from "../components/MessagePage";
import PrivateRoute from "./PrivateRoute";

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
                element: <PrivateRoute element={<Home/>}/>,
                children: [
                    {
                        path: 'chat-rooms/:roomId',
                        element: <PrivateRoute element={<MessagePage />} />
                    }
                ]
            }
        ]
    }
])

export default router