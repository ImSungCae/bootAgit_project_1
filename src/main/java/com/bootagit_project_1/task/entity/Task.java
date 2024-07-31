package com.bootagit_project_1.task.entity;

import com.bootagit_project_1.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @NotNull
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotNull
    @NotBlank(message = "할 일을 입력해주세요.")
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    private boolean completed;

    @NotNull
    private LocalDate dueDate;

    private LocalDate completedDate;


    public Long getDaysUntilDue(){
        if(completedDate != null){
            return ChronoUnit.DAYS.between(createdAt.toLocalDate(), completedDate);
        } else if (completed) {
            return ChronoUnit.DAYS.between(createdAt.toLocalDate(),LocalDate.now());
        }else {
            return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        }
    }



}
