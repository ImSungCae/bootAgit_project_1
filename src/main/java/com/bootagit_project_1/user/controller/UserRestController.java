package com.bootagit_project_1.user.controller;

import com.bootagit_project_1.user.dto.ChangePasswordDto;
import com.bootagit_project_1.user.dto.UserDto;
import com.bootagit_project_1.user.dto.UserProfileDto;
import com.bootagit_project_1.user.entity.UserProfile;
import com.bootagit_project_1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    /* 회원 탈퇴 API */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /* 프로필 조회 API */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            UserProfileDto userProfileDTO = userService.getUserProfile(username);
            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /* 프로필 수정 API */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("statusMessage") String statusMessage) throws IOException {
        String username = userDetails.getUsername();
        System.out.println(username);
        System.out.println(profileImage);
        System.out.println(statusMessage);
        return ResponseEntity.ok(userService.updateProfile(username,profileImage,statusMessage));
    }

    /* 친구 추가 API */
    @PostMapping("/friend")
    public ResponseEntity<Void> addFriend(
            @RequestParam("username") String username,
            @RequestParam("friendUsername") String friendUsername){
        userService.addFriend(username,friendUsername);
        return ResponseEntity.ok().build();
    }

    /* 친구 삭제 API */
    @DeleteMapping("/friend")
    public ResponseEntity<Void> removeFriend(
            @RequestParam("username") String username,
            @RequestParam("friendUsername") String friendUsername){
        userService.removeFriend(username,friendUsername);
        return ResponseEntity.ok().build();
    }

    /* 친구 목록 조회 API */
    @GetMapping("/friend")
    public ResponseEntity<List<UserDto>> getFriends(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.getFriendList(username));
    }


}
