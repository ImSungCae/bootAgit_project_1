package com.bootagit_project_1.user.dto;

import com.bootagit_project_1.user.entity.User;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;

    static public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }
    public User toEntity() {
        return User.builder()
                .id(getId())
                .username(getUsername())
                .password(getPassword())
                .email(getEmail())
                .build();
    }
}
