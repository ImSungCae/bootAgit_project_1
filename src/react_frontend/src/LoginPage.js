import {useState} from "react";
import {login} from "./AuthAPI";
import 'bootstrap/dist/css/bootstrap.min.css';

export default function LoginPage(){
    const [values, setValues] = useState({
        username : "",
        password : "",
    });

    const handleChange = async(e) =>{
        setValues({...values,
            [e.target.name]: e.target.value,
        });
    }

    const handleSubmit = async(e) =>{
        e.preventDefault();
        try{
            const response = await login(values); // 로그인 요청
            localStorage.clear();
            localStorage.setItem('grantType', response.grantType);
            localStorage.setItem('accessToken', response.accessToken);
            localStorage.setItem('refreshToken', response.refreshToken);
            window.location.href = `/`;
        }catch(error){
            alert(error.message);
            console.log(error);
        }

    }

    return (
            <div className="d-flex justify-content-center" style={{minHeight: "100vh"}}>
                <div className="align-self-center">
                    <form onSubmit={handleSubmit}>
                        <div className="form-group" style={{minWidth: "25vw"}}>
                            <label htmlFor="username">아이디</label>
                            <input type="text" className="form-control" id="username" name="username" required
                                   onChange={handleChange} value={values.username}/>
                        </div>
                        <div className="form-group" style={{minWidth: "25vw"}}>
                            <label htmlFor="password">비밀번호</label>
                            <input type="password" className="form-control" id="password" name="password" required
                                   onChange={handleChange} value={values.password}/>
                        </div>
                        <div className="form-group" style={{minWidth: "25vw"}}>
                            <button type="submit" style={{width: "100%"}}>로그인</button>
                        </div>
                    </form>
                </div>
            </div>

    );
}