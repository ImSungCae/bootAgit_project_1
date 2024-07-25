import axios from "axios";

const GRANT_TYPE = localStorage.getItem("grantType");
let ACCESS_TOKEN = localStorage.getItem("accessToken");
let REFRESH_TOKEN = localStorage.getItem("refreshToken");

export const UserApi = axios.create({
    baseURL: 'http://localhost:8080/api/v1/user',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `${GRANT_TYPE} ${ACCESS_TOKEN}`,
        'REFRESH_TOKEN': REFRESH_TOKEN,
    },
});

/* 회원조회 API */
export const fetchUserProfile = async () =>{
    try{
        const response = await UserApi.get(`/profile`);
        return response.data;
    }catch (error){
        console.log("사용자를 찾을 수 없습니다. ",error);
        throw error;
    }
}
/* 패스워드 변경 API  */
export const changePassword = async ({ oldPassword, newPassword}) =>{
    const data = {  oldPassword, newPassword };
    const response = await UserApi.post(`/password`,data);
    return response.data;
}

/* 이메일 변경 API */
export const changeEmail = async (newEmail)=>{

    const response = await UserApi.post(`/email`,null, { params : { newEmail}});
    return response.data;
}

/* 회원 탈퇴 API */
export const deleteUser = async () =>{
    await UserApi.delete(`/delete`);
}
