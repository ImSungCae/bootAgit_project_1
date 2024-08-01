package com.bootagit_project_1.user.repository;

import com.bootagit_project_1.user.entity.Friend;
import com.bootagit_project_1.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(User user);
}
