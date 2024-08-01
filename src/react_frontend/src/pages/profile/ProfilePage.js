import React, { useState, useEffect } from "react";
import { useUser } from "../common/AuthContext";
import {updateProfile, getFriendList, getProfile} from "../api/UserAPI";
import ChangePassword from "./ChangePassword";
import ChangeEmail from "./ChangeEmail";
import { deleteUser } from "../api/UserAPI";
import { FaImage } from "react-icons/fa6";
import "./ProfilePage.css"

const ProfilePage = () => {
    const { authState } = useUser();
    const { username } = authState;
    const [profileImage, setProfileImage] = useState(null);
    const [statusMessage, setStatusMessage] = useState('');
    const [friends, setFriends] = useState([]);
    const [profileImageUrl, setProfileImageUrl] = useState(null);

    useEffect(() => {
        loadFriends();
        // Load user profile data on mount
        loadUserProfile();
    }, []);

    const loadFriends = async () => {
        try {
            const friendList = await getFriendList();
            setFriends(friendList);
        } catch (error) {
            console.error('친구 목록을 불러오는 중 오류가 발생했습니다.', error);
        }
    };

    const loadUserProfile = async () => {
        try {
            // getProfile 함수의 반환값을 확인합니다.
            const data = await getProfile();





            // JSON 구조가 예상과 일치하는지 확인합니다.
            if (data.statusMessage && data.profileImageUrl) {
                setStatusMessage(data.statusMessage);
                setProfileImageUrl(data.profileImageUrl);
            } else {
                throw new Error('API 응답 형식이 예상과 다릅니다.');
            }

        } catch (error) {
            // 더 구체적인 에러 메시지를 로그에 포함시킵니다.
            console.error('프로필 정보를 불러오는 중 오류가 발생했습니다.', error.message);
        }
    };

    const handleProfileUpdate = async (e) => {
        e.preventDefault();
        try {

            await updateProfile(profileImage,statusMessage);
            alert('프로필이 업데이트되었습니다.');
            // Optionally reload profile data
            loadUserProfile();
        } catch (error) {
            console.error('프로필 업데이트 중 오류가 발생했습니다.', error);
            alert('프로필 업데이트에 실패했습니다.');
        }
    };

    const handleDeleteAccount = async () => {
        try {
            await deleteUser();
            localStorage.clear();
            alert("회원 탈퇴가 완료되었습니다.");
            window.location.href = "/";
        } catch (error) {
            console.error("회원 탈퇴 중 오류가 발생했습니다.", error.response?.data?.message || error.message);
            alert("회원 탈퇴에 실패했습니다: " + (error.response?.data?.message || error.message));
        }
    };

    return (
        <div className="container mt-5">
            <h2>프로필</h2>
            <form onSubmit={handleProfileUpdate}>
                <div className="mb-3">
                    <label className="form-label">프로필 이미지</label>
                    <div className="profile-image-container">
                        <img
                            src={profileImageUrl ? `http://localhost:8080/files/${profileImageUrl}` : '/default-profile.png'}
                            alt="Profile"
                            className="profile-image"
                        />
                        <FaImage
                            className="profile-image-icon"
                            onClick={() => document.getElementById('profileImageInput').click()}
                        />
                        <input
                            type="file"
                            id="profileImageInput"
                            style={{ display: 'none' }}
                            onChange={(e) => setProfileImage(e.target.files[0])}
                        />
                    </div>
                </div>
                <div className="mb-3">
                    <label htmlFor="statusMessage" className="form-label">상태 메시지</label>
                    <input
                        type="text"
                        className="form-control"
                        id="statusMessage"
                        value={statusMessage}
                        onChange={(e) => setStatusMessage(e.target.value)}
                    />
                </div>
                <button type="submit" className="btn btn-primary">프로필 업데이트</button>
            </form>

            <ChangePassword />
            <ChangeEmail />

            <h3 className="mt-5">친구 목록</h3>
            <ul className="list-group">
                {friends.map((friend) => (
                    <li key={friend.id} className="list-group-item">{friend.username}</li>
                ))}
            </ul>

            <button
                type="button"
                className="btn btn-danger mt-3"
                onClick={handleDeleteAccount}
            >
                회원 탈퇴
            </button>
        </div>
    );
};

export default ProfilePage;
