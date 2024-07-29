import logo from '../../logo.svg';
import React from "react";
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";
import { useUser } from "./AuthContext";



export default function NavBar() {
    const { authState, logout } = useUser();
    const { token, username } = authState;
    const handleLogout = async ()=>{
        logout();
    }

    return(
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand href="/">
                    <img src={logo} width="40" height="35" alt="" />
                    BootAgit
                </Navbar.Brand>

                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto" alt="Nav Empty Space">

                    </Nav>
                    <Nav>
                        <NavDropdown title="메뉴" id="collasible-nav-dropdown">
                            <NavDropdown.Item href={token? "/task" : "/login"}>할 일</NavDropdown.Item>
                            <NavDropdown.Item href={token? "/chat" : "/login"}>채팅</NavDropdown.Item>
                        </NavDropdown>

                        {token ?
                            <NavDropdown title={username + "님 환영합니다."} id="collasible-nav-dropdown">
                                <NavDropdown.Item href="/profile">프로필</NavDropdown.Item>
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