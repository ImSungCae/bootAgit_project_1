import axios from "axios";

const GRANT_TYPE = localStorage.getItem("grantType");
let ACCESS_TOKEN = localStorage.getItem("accessToken");

export const AuthApi = axios.create({
    baseURL : 'http://localhost:8080',
    headers : {
        'Content-Type' : 'application/json',
        'Authorization' : `${GRANT_TYPE} ${ACCESS_TOKEN}`,
    },
});

/* LOGIN API */
export const login = async ({username, password}) =>{
    const data = {username, password};
    const response = await AuthApi.post(`/api/user/login`,data);
    return response.data;
}

/* REGISTER API */
export const register = async ({username, password, email}) =>{
    const data = {username, password, email};
    const response = await AuthApi.post(`/api/user/register`,data);
    return response.data;
}

/* FETCH USER API*/
export const fetchUserProfile = async () =>{
    try{
        const response = await AuthApi.get(`/api/user/profile`);
        return response.data;
    }catch (error){
        console.log("사용자를 찾을 수 없습니다. ",error);
        throw error;
    }
}