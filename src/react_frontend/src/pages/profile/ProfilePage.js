import {Fragment} from "react";
import ChangePassword from "./ChangePassword";
import ChangeEmail from "./ChangeEmail";
import {deleteUser} from "../api/UserAPI";


const ProfilePage = () => {
    const handleDeleteAccount = async ()=>{
        try{
            await deleteUser();
            localStorage.clear();
            alert("회원 탈퇴가 완료되었습니다.");
            window.location.href = "/";
        }catch(error){
            console.error("회원 탈퇴 중 오류가 발생했습니다.", error.response?.data?.message || error.message);
            alert("회원 탈퇴에 실패했습니다: " + (error.response?.data?.message || error.message));
        }
    }
    return (
        <Fragment>
            <div className="container mt-5">
                <h1>프로필</h1>

            <ChangePassword/>
            <ChangeEmail/>
            <button
                type="button"
                className="btn btn-danger mt-3"
                onClick={handleDeleteAccount}
            >
                회원 탈퇴
            </button>
            </div>
        </Fragment>
    );
};

export default ProfilePage;