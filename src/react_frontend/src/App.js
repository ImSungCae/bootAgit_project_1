// App.js
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Layout from "./pages/common/Layout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/user/LoginPage";
import RegisterPage from "./pages/user/RegisterPage";
import ProfilePage from "./pages/profile/ProfilePage";
import TaskPage from "./pages/task/TaskPage";

const App = () => {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/task/:username" element={<TaskPage />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;