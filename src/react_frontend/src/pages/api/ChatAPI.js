import axios from 'axios';

const apiUrl = process.env.REACT_APP_API_URL;
const GRANT_TYPE = localStorage.getItem("grantType");
const ACCESS_TOKEN = localStorage.getItem("accessToken");
const REFRESH_TOKEN = localStorage.getItem("refreshToken");

export const ChatApi = axios.create({
    baseURL: `${apiUrl}`,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `${GRANT_TYPE} ${ACCESS_TOKEN}`,
        'REFRESH_TOKEN': REFRESH_TOKEN,
    }
});

export const getChatRooms = () => {
    return ChatApi.get('/api/chatrooms');
};

export const createChatRoom = (roomName, maxUsers, isPublic, password) => {
    return ChatApi.post('/api/chatrooms', {
        name: roomName,
        maxUsers: maxUsers,
        isPublic: isPublic,
        password: isPublic ? '' : password
    });
};

export const getChatRoomById = async (roomId) => {
    try{
        const response = await ChatApi.get(`/api/chatrooms/${roomId}`);
        return response.data;
    }catch(error){
        console.log(error);
        throw error;
    }
};
