package com.bootagit_project_1.user.controller;


import com.bootagit_project_1.utils.SecurityUtil;
import com.bootagit_project_1.jwt.JwtToken;
import com.bootagit_project_1.user.dto.LoginDto;
import com.bootagit_project_1.user.dto.RegisterDto;
import com.bootagit_project_1.user.dto.UserDto;
import com.bootagit_project_1.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API")
public class AuthRestController {

    private final AuthService authService;


    /* 로그인 API */
    @PostMapping("/login")
    public JwtToken loginUser(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        JwtToken jwtToken = authService.login(username, password);
        return jwtToken;
    }

    /* 회원가입 API */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterDto registerDto) {
        UserDto userDto = authService.registerUser(registerDto);
        return ResponseEntity.ok(userDto);
    }

    /* 토큰갱신 API */



    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }



}
