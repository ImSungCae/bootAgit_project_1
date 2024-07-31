// components/ProtectedRoute.js
import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";

const ProtectedRoute = ({ component: Component }) => {
    const token = localStorage.getItem("accessToken");
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            alert("로그인 후 사용해주세요.");
            navigate("/login");
        }
    }, [token,navigate]);

    if(!token){
        return null;
    }

    return <Component />;
};

export default ProtectedRoute;
