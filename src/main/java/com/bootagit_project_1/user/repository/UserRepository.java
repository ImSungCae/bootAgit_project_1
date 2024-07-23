package com.bootagit_project_1.user.repository;

import com.bootagit_project_1.user.entity.User_table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User_table,Long> {
    Optional<User_table> findByUsername(String username);
    boolean existsByUsername(String username);

}
