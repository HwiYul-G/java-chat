import { Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";


const PrivateRoute = ({element}) => {
    const { userInfo } = useUser();

    if (!userInfo || !userInfo.id) {
        return <Navigate to="/login" replace />;
    }

    return element;
};

export default PrivateRoute;