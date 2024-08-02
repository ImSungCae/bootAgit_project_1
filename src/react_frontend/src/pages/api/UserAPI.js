import axios from "axios";

const apiUrl = process.env.REACT_APP_API_URL;
const GRANT_TYPE = localStorage.getItem("grantType");
let ACCESS_TOKEN = localStorage.getItem("accessToken");
let REFRESH_TOKEN = localStorage.getItem("refreshToken");

export const UserApi = axios.create({
    baseURL: `${apiUrl}/api/v1/user`,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `${GRANT_TYPE} ${ACCESS_TOKEN}`,
        'REFRESH_TOKEN': REFRESH_TOKEN,
    },
});


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

/* 프로필 조회 API */
export const getProfile = async () => {
    const response = await UserApi.get(`/profile`)
    console.log('응답 데이터:', response.data);
    console.log('응답 상태 코드:', response.status);
    return response.data;
}

/* 프로필 수정 API */
export const updateProfile = async (profileImage, statusMessage) =>{
    const formData = new FormData();
    formData.append('profileImage', profileImage);
    formData.append('statusMessage', statusMessage);


    const response = await UserApi.put(`/profile`,formData,{
        headers:{
            'Content-Type': 'multipart/form-data',
        },
    });
    return response.data;
}

/* 친구 추가 API */
export const addFriend = async (username, friendUsername) => {
    const response = await UserApi.post(`/friend`,null, { params : { friendUsername }});
    return response.data;
}

/* 친구 삭제 API */
export const removeFriend = async (username, friendUsername) => {
    const response = await UserApi.delete(`/friend`, {
        params: {username, friendUsername},
    });
    return response.data;
}

/* 친구 목록 조회 API */
export const getFriendList = async () => {
    const response = await UserApi.get(`/friend`);
    return response.data;
}