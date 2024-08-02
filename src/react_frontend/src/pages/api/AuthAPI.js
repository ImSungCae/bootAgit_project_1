import axios from "axios";

const apiUrl = process.env.REACT_APP_API_URL;
const GRANT_TYPE = localStorage.getItem("grantType");
let ACCESS_TOKEN = localStorage.getItem("accessToken");

export const AuthApi = axios.create({
    baseURL : `${apiUrl}/api/v1/auth`,
    headers : {
        'Content-Type' : 'application/json',
        'Authorization' : `${GRANT_TYPE} ${ACCESS_TOKEN}`,
    },
});

/* LOGIN API */
export const login = async ({username, password}) =>{
    const data = {username, password};
    const response = await AuthApi.post(`/login`,data);
    return response.data;
}

/* REGISTER API */
export const register = async ({username, password, email}) =>{
    const data = {username, password, email};
    const response = await AuthApi.post(`/register`,data);
    return response.data;
}

