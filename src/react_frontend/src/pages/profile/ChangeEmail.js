import React, { useState } from 'react';
import { changeEmail } from '../api/UserAPI';
import 'bootstrap/dist/css/bootstrap.min.css';

const ChangeEmail = () => {
    const [newEmail, setNewEmail] = useState('');
    const [confirmNewEmail, setConfirmNewEmail] = useState('');

    const handleChangeEmail = async () => {
        if (newEmail !== confirmNewEmail) {
            alert("새 이메일이 일치하지 않습니다.");
            return;
        }

        try {
            await changeEmail( newEmail );
            alert("이메일이 성공적으로 변경되었습니다.");
        } catch (error) {
            console.error("이메일 변경 중 오류가 발생했습니다.", error);
            alert("이메일 변경에 실패했습니다.");
        }
    };

    return (
            <form>
                <h3 className="mt-3">이메일 변경</h3>
                <div className="form-group">
                    <label>새 이메일</label>
                    <input
                        type="email"
                        className="form-control"
                        value={newEmail}
                        onChange={(e) => setNewEmail(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>새 이메일 확인</label>
                    <input
                        type="email"
                        className="form-control"
                        value={confirmNewEmail}
                        onChange={(e) => setConfirmNewEmail(e.target.value)}
                    />
                </div>
                <button
                    type="button"
                    className="btn btn-primary mt-3"
                    onClick={handleChangeEmail}
                >
                    이메일 변경
                </button>
            </form>
    );
};

export default ChangeEmail;