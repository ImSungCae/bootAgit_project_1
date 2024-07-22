package com.bootagit_project_1.user.controller;


import com.bootagit_project_1.config.SecurityUtil;
import com.bootagit_project_1.jwt.JwtToken;
import com.bootagit_project_1.user.entity.LoginDto;
import com.bootagit_project_1.user.entity.User;
import com.bootagit_project_1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public ModelAndView registerUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view/user/register.html");
        return modelAndView;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully.");
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

    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }


}
