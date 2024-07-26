import React, {createContext, useContext, useEffect, useState} from 'react';
import {jwtDecode} from "jwt-decode";

// Create a Context for the user
const AuthContext = createContext();

// Create a Provider component
export const AuthProvider = ({ children }) => {
    const [authState, setAuthState] = useState({
        username: '',
        email: '',
        token: ''
    });
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if(token){
            try{
                const decodedToken = jwtDecode(token);
                setAuthState({
                    username: decodedToken.username,
                    email: decodedToken.email,
                    token: token
                });
            }catch(error){
                console.log(error);
            }
        }
    }, []);

    const login = (token) => {
        localStorage.setItem('accessToken', token);
        const decodedToken = jwtDecode(token);
        setAuthState({
            username: decodedToken.username,
            email: decodedToken.email,
            token: token
        });
    };

    const logout = () => {
        localStorage.clear();
        window.location.href = "/";
    };
    return (
        <AuthContext.Provider value={{ authState, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// Create a custom hook for easier context usage
export const useUser = () => useContext(AuthContext);
