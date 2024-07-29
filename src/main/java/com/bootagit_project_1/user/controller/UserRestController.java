package com.bootagit_project_1.user.controller;

import com.bootagit_project_1.user.dto.ChangePasswordDto;
import com.bootagit_project_1.user.dto.UserDto;
import com.bootagit_project_1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRestController {
    private final UserService userService;

    /* 패스워드 변경 API */
    @PostMapping("/password")
    public ResponseEntity<UserDto> setUserPassword(@RequestBody ChangePasswordDto request) {
        return ResponseEntity.ok(userService.changeUserPassword(request.getOldPassword(),request.getNewPassword()));
    }

    /* 이메일 변경 API */
    @PostMapping("/email")
    public ResponseEntity<UserDto> setUserEmail(@RequestParam("newEmail") String newEmail) {
        return ResponseEntity.ok(userService.changeUserEmail(newEmail));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
