package com.bootagit_project_1.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    private String profileImageUrl;
    private String statusMessage;
}
