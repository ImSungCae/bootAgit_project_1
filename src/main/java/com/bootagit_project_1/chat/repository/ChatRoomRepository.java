package com.bootagit_project_1.chat.repository;

import com.bootagit_project_1.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long > {
}
