package com.bootagit_project_1.user.service;

import com.bootagit_project_1.user.dto.UserDto;
import com.bootagit_project_1.user.dto.UserProfileDto;
import com.bootagit_project_1.user.entity.Friend;
import com.bootagit_project_1.user.entity.User;
import com.bootagit_project_1.user.entity.UserProfile;
import com.bootagit_project_1.user.repository.FriendRepository;
import com.bootagit_project_1.user.repository.UserProfileRepository;
import com.bootagit_project_1.user.repository.UserRepository;
import com.bootagit_project_1.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    /* USER 조회 */
    @Transactional
    public UserProfileDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId());

        return UserProfileDto.builder()
                .id(userProfile.getId())
                .profileImageUrl(userProfile.getProfileImageUrl())
                .statusMessage(userProfile.getStatusMessage())
                .build();
    }

    /* 패스워드 변경 */
    @Transactional
    public UserDto changeUserPassword(String oldPassword, String newPassword){
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode((newPassword)));
        return UserDto.toDto(userRepository.save(user));

    }

    /* 이메일 변경 */
    @Transactional
    public UserDto changeUserEmail(String newEmail){
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        user.setEmail(newEmail);
        return UserDto.toDto(userRepository.save(user));
    }

    /* 회원 탈퇴 */
    @Transactional
    public void deleteUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다. username = "+ username));
        this.userRepository.delete(user);

    }

    /* 프로필 수정 */
    @Transactional
    public UserDto updateProfile(String username, MultipartFile profileImage, String statusMessage) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        if(profile == null){
            profile = new UserProfile();
            profile.setUser(user);
        }

        if(profileImage!=null && !profileImage.isEmpty()){
            try {
                // 파일 저장 경로
                String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                // 디렉토리 생성
                Files.createDirectories(filePath.getParent());

                // 파일 저장
                Files.write(filePath, profileImage.getBytes());
                profile.setProfileImageUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생", e);
            }
        }

        profile.setStatusMessage(statusMessage);
        userProfileRepository.save(profile);

        return UserDto.toDto(user);
    }

    /* 친구 추가 */
    @Transactional
    public void addFriend(String username, String friendUsername){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("친구를 찾을 수 없습니다."));

        Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriend(friend);
        friendRepository.save(newFriend);
    }

    /* 친구 삭제 */
    @Transactional
    public void removeFriend(String username, String friendUsername){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("친구를 찾을 수 없습니다."));

        Friend friendRelation = friendRepository.findByUser(user).stream()
                .filter(f -> f.getFriend().equals(friend))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("친구 관계를 찾을 수 없습니다."));

        friendRepository.delete(friendRelation);
    }

    /* 친구 목록 조회 */
    public List<UserDto> getFriendList(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return friendRepository.findByUser(user).stream()
                .map(friend -> UserDto.toDto(friend.getFriend()))
                .collect(Collectors.toList());
    }

    /* Elastic Search 친구 조회 */
//    public List<Friend> search(@RequestParam("username") String username){
//        return friendRepository.findByUsername(username);
//    }



}
