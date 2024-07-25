import logo from '../../logo.svg';
import React, {useEffect,useState} from "react";
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {fetchUserProfile} from "../api/UserAPI";


export default function NavBar() {
    const [user, setUser] = useState({});
    const ACCESS_TOKEN = localStorage.getItem("accessToken");

    useEffect(() => {
        if(ACCESS_TOKEN){
            fetchUserProfile()
                .then((response) => {
                    setUser(response);
                }).catch((error) => {
                    console.log(error);
            });
        }
    }, [ACCESS_TOKEN, setUser]);

    const handleLogout = async ()=>{
        localStorage.clear();
        window.location.href = "/";
    }
    const username = user?.username || '회원님';
    return(
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand href="/">
                    <img src={logo} width="40" height="35" alt="" />
                    NavBar
                </Navbar.Brand>

                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto" alt="Nav Empty Space">

                    </Nav>
                    <Nav>
                        <NavDropdown title="메뉴" id="collasible-nav-dropdown">
                            <NavDropdown.Item href={`/task/${username}`}>할 일</NavDropdown.Item>
                            <NavDropdown.Item href="/">채팅</NavDropdown.Item>
                        </NavDropdown>

                        {ACCESS_TOKEN ?
                            <NavDropdown title={user.username + "님 환영합니다."} id="collasible-nav-dropdown">
                                <NavDropdown.Item href="/profile/">프로필</NavDropdown.Item>
                                <NavDropdown.Item href="/" onClick={handleLogout}>로그아웃</NavDropdown.Item>
                            </NavDropdown>
                            :
                            <NavDropdown title="로그인/회원가입" id="collasible-nav-dropdown">
                                <NavDropdown.Item href="/login">로그인</NavDropdown.Item>
                                <NavDropdown.Item href="/register">회원가입</NavDropdown.Item>
                            </NavDropdown>

                        }

                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>



    );
}