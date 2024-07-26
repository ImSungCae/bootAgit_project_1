package com.bootagit_project_1.user.service;

import com.bootagit_project_1.user.dto.UserDto;
import com.bootagit_project_1.user.entity.User;
import com.bootagit_project_1.user.repository.UserRepository;
import com.bootagit_project_1.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* USER 조회 */
    @Transactional
    public UserDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserDto.toDto(user);
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

}
