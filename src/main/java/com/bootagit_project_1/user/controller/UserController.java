package com.bootagit_project_1.user.controller;


import com.bootagit_project_1.jwt.JwtTokenProvider;
import com.bootagit_project_1.utils.SecurityUtil;
import com.bootagit_project_1.jwt.JwtToken;
import com.bootagit_project_1.user.entity.LoginDto;
import com.bootagit_project_1.user.entity.RegisterDto;
import com.bootagit_project_1.user.entity.UserDto;
import com.bootagit_project_1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterDto registerDto) {
        UserDto userDto = userService.registerUser(registerDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public JwtToken loginUser(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        JwtToken jwtToken = userService.login(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }


    @GetMapping("/profile")
    public UserDto getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserProfile(username);
    }

    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }


}
