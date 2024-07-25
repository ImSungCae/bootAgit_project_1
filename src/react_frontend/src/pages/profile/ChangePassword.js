import React, { useState } from 'react';
import { changePassword } from '../api/UserAPI';
import 'bootstrap/dist/css/bootstrap.min.css';

const ChangePassword = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const handleChangePassword = async () => {
        if(newPassword !== confirmNewPassword){
            alert("새 비밀번호가 일치하지 않습니다.");
            return;
        }

        try{
            await changePassword({ oldPassword, newPassword});
            alert("비밀번호가 성공적으로 변경되었습니다.");
        }catch(error){
            console.log("비밀번호 변경 중 오류가 발생했습니다.", error);
            alert("비밀번호 변경에 실패했습니다.");
        }
    };

    return (
        <form>
            <h3 className="mt-3">비밀번호 변경</h3>
            <div className="form-group">
                <label>현재 비밀번호</label>
                <input
                    type="password"
                    className="form-control"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                />
            </div>
            <div className="form-group">
                <label>새 비밀번호</label>
                <input
                    type="password"
                    className="form-control"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                />
            </div>
            <div className="form-group">
                <label>새 비밀번호 확인</label>
                <input
                    type="password"
                    className="form-control"
                    value={confirmNewPassword}
                    onChange={(e) => setConfirmNewPassword(e.target.value)}
                />
            </div>
            <button
                type="button"
                className="btn btn-primary mt-3"
                onClick={handleChangePassword}
            >
                비밀번호 변경
            </button>
        </form>
    );
};
export default ChangePassword;