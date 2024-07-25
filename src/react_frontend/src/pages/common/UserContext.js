import React, { createContext, useContext, useState } from 'react';

// Create a Context for the user
const UserContext = createContext();

// Create a Provider component
export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    return (
        <UserContext.Provider value={{ user, setUser }}>
            {children}
        </UserContext.Provider>
    );
};

// Create a custom hook for easier context usage
export const useUser = () => useContext(UserContext);
