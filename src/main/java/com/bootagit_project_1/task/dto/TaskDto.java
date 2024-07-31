package com.bootagit_project_1.task.dto;

import com.bootagit_project_1.task.entity.Task;
import com.bootagit_project_1.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean completed;
    private LocalDate dueDate;
    private LocalDate completedDate;
    private Long daysUntilDue;

    public static TaskDto fromEntity(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completed(task.isCompleted())
                .dueDate(task.getDueDate())
                .completedDate(task.getCompletedDate())
                .daysUntilDue(task.getDaysUntilDue())
                .build();
    }

    public static Task toEntity(TaskDto dto, User user) {
        return Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .completed(dto.isCompleted())
                .user(user)
                .dueDate(dto.getDueDate())
                .completedDate(dto.getCompletedDate())
                .build();
    }
}
