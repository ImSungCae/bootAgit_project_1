import axios from 'axios';

const GRANT_TYPE = localStorage.getItem("grantType");
let ACCESS_TOKEN = localStorage.getItem("accessToken");
let REFRESH_TOKEN = localStorage.getItem("refreshToken");

export const TaskApi = axios.create({
    baseURL : 'http://localhost:8080/api/v1/task',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `${GRANT_TYPE} ${ACCESS_TOKEN}`,
        'REFRESH_TOKEN': REFRESH_TOKEN,
    },
});

/* 할 일 목록 조회 API */
export const fetchTasks = async (username) => {
    try{
        const response = await TaskApi.get(``);
        return response.data;
    }catch(error){
        console.log("할 일을 불러오는 데 실패했습니다.", error);
        throw error;
    }
};

// 할 일 생성 API
export const createTask = async (task, username) => {
    try {
        console.log(task);
        const response = await TaskApi.post(``, task);
        return response.data;
    } catch (error) {
        console.log("할 일을 생성하는 데 실패했습니다.", error);
        throw error;
    }
};

// 할 일 수정 API
export const updateTask = async (taskId, task, username) => {
    try {
        const response = await TaskApi.put(`/${taskId}`, task);
        return response.data;
    } catch (error) {
        console.log("할 일을 수정하는 데 실패했습니다.", error);
        throw error;
    }
};

// 할 일 삭제 API
export const deleteTask = async (taskId, username) => {
    try {
        const response = await TaskApi.delete(`/${taskId}`);
        return response.data;
    } catch (error) {
        console.log("할 일을 삭제하는 데 실패했습니다.", error);
        throw error;
    }
};