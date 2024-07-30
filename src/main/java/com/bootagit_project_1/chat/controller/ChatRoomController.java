package com.bootagit_project_1.chat.controller;

import com.bootagit_project_1.chat.entity.ChatRoom;
import com.bootagit_project_1.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatRoom> getChatRooms(){
        return chatRoomService.getAllChatRooms();
    }

    @PostMapping
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom){
        return chatRoomService.createChatRoom(chatRoom);
    }
}
