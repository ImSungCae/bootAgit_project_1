package com.bootagit_project_1.task.service;

import com.bootagit_project_1.task.dto.TaskDto;
import com.bootagit_project_1.task.entity.Task;
import com.bootagit_project_1.task.repository.TaskRepository;
import com.bootagit_project_1.user.entity.User;
import com.bootagit_project_1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TaskDto> getTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. username = " + username));
        return taskRepository.findByUserId(user.getId()).stream()
                .map(TaskDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskDto createTask(String username, TaskDto taskDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. username = " + username));
        Task task = TaskDto.toEntity(taskDto, user);
        return TaskDto.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public TaskDto updateTask(String username, Long taskId, TaskDto taskDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. username = " + username));
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. id = " + taskId));
        if (!existingTask.getUser().equals(user)) {
            throw new RuntimeException("사용자가 할 일을 수정할 권한이 없습니다.");
        }
        Task updatedTask = Task.builder()
                .id(existingTask.getId())
                .user(existingTask.getUser())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .createdAt(existingTask.getCreatedAt())
                .updatedAt(taskDto.getUpdatedAt())
                .completed(taskDto.isCompleted())
                .dueDate(taskDto.getDueDate())
                .completedDate(taskDto.isCompleted() ? LocalDate.now() : null)
                .build();
        return TaskDto.fromEntity(taskRepository.save(updatedTask));
    }

    @Transactional
    public void deleteTask(String username, Long taskId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. username = " + username));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("할 일을 찾을 수 없습니다. id = " + taskId));
        if (!task.getUser().equals(user)) {
            throw new RuntimeException("사용자가 할 일을 삭제할 권한이 없습니다.");
        }
        taskRepository.delete(task);
    }

}
