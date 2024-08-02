package com.bootagit_project_1.task.controller;

import com.bootagit_project_1.task.dto.TaskDto;
import com.bootagit_project_1.task.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Task API")
public class TaskRestController {
    private final TaskService taskService;


    // 사용자가 가진 모든 작업 목록을 반환합니다.
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<TaskDto> tasks = taskService.getTasks(username);
        return ResponseEntity.ok(tasks);
    }

    // 사용자가 새로운 작업을 생성합니다.
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TaskDto taskDto) {
        String username = userDetails.getUsername();
        TaskDto createdTask = taskService.createTask(username, taskDto);
        return ResponseEntity.ok(createdTask);
    }

    // 사용자가 작업을 수정합니다.
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody TaskDto taskDto) {
        String username = userDetails.getUsername();
        TaskDto updatedTask = taskService.updateTask(username, id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    // 사용자가 작업을 삭제합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        String username = userDetails.getUsername();
        taskService.deleteTask(username, id);
        return ResponseEntity.noContent().build();
    }
}
