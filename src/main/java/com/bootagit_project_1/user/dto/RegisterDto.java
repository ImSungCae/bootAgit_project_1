package com.bootagit_project_1.user.dto;

import com.bootagit_project_1.user.entity.User_table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    private String username;
    private String password;
    private String email;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public User_table toEntity(String encodedPassword, List<String> roles){
        return User_table.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .roles(roles)
                .build();
    }
}
