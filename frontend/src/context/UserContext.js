import { createContext, useContext, useState } from "react";

const UserContext = createContext();

export const UserProvider = ({children}) => {
    // userInfo : {id: 1, email: 'a@google.com', username: 'a', enabled: true, roles: 'admin user'}
    const [userInfo, setUserInfo] = useState(null);

    return (
        <UserContext.Provider value={{userInfo, setUserInfo}}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => {
    return useContext(UserContext);
};