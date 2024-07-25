import {useState} from "react";
import {register} from "../api/AuthAPI";
import 'bootstrap/dist/css/bootstrap.min.css';

export default function RegisterPage() {
    const [values,setValues] = useState({
        username : "",
        password : "",
        email : "",
    });
    const handleChange = async(e) =>{
        setValues({...values,
            [e.target.name]: e.target.value,
        });
    }
    const handleSubmit = async(e) =>{
        e.preventDefault();
        try{
            register(values)
            window.location.href="/login";
        }catch (error){
            alert(error.message);
            console.log(error);
        }


    }
    return (
        <div className="d-flex justify-content-center" style={{minHeight: "80vh"}}>
            <div className="align-self-center">
                <h1 className="text-center">회원가입</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-group" style={{minWidth: "25vw"}}>
                        <label htmlFor="username">아이디</label>
                        <input type="text" className="form-control" id="username" name="username"
                               onChange={handleChange}
                               value={values.username}/>
                    </div>
                    <div className="form-group" style={{minWidth: "25vw"}}>
                        <label htmlFor="password">비밀번호</label>
                        <input type="password" className="form-control" id="password" name="password"
                               onChange={handleChange}
                               value={values.password}/>
                    </div>
                    <div className="form-group" style={{minWidth: "25vw"}}>
                        <label htmlFor="email">이메일</label>
                        <input type="email" className="form-control" id="email" name="email"
                               onChange={handleChange}
                               value={values.email}/>
                    </div>
                    <div className="form-group" style={{minWidth: "25vw", marginTop: "1vw"}}>
                        <button type="submit" style={{width: "100%"}}>회원가입</button>
                    </div>
                </form>
            </div>
        </div>
    )

}