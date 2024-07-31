package com.bootagit_project_1.chat.service;

import com.bootagit_project_1.chat.entity.ChatRoom;
import com.bootagit_project_1.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }
    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }
}
