// App.js
import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Layout from "./pages/common/Layout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/user/LoginPage";
import RegisterPage from "./pages/user/RegisterPage";
import ProfilePage from "./pages/profile/ProfilePage";
import TaskPage from "./pages/task/TaskPage";
import ChatRoomList from "./pages/chat/ChatRoomList"
import ChatRoom from "./pages/chat/ChatRoom"
import ProtectedRoute from "./pages/common/ProtectedRoute";

const App = () => {

    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/profile" element={<ProtectedRoute component={ProfilePage} />} />
                    <Route path="/task" element={<ProtectedRoute component={TaskPage} />} />
                    <Route path="/chat" element={<ProtectedRoute component={ChatRoomList} />} />
                    <Route path="/chat/:roomId" element={<ProtectedRoute component={ChatRoom} />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
