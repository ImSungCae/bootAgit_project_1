package com.bootagit_project_1.task.controller;

import com.bootagit_project_1.task.dto.TaskDto;
import com.bootagit_project_1.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class TaskRestController {
    private final TaskService taskService;


    @GetMapping("/{username}")
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable String username ) {

        return ResponseEntity.ok(taskService.getTasks(username));
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TaskDto taskDto) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(taskService.createTask(username, taskDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody TaskDto taskDto) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(taskService.updateTask(username, id, taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        String username = userDetails.getUsername();
        taskService.deleteTask(username, id);
        return ResponseEntity.noContent().build();
    }
}
