package com.bootagit_project_1.chat.controller;

import com.bootagit_project_1.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(ChatMessage message, @DestinationVariable String roomId) {
        message.setRoomId(roomId); // roomId 설정
        System.out.println(roomId);
        return message;
    }

    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage addUser(ChatMessage message, @DestinationVariable String roomId) {
        message.setContent(message.getSender() + " 님이 입장하셨습니다.");
        message.setRoomId(roomId); // roomId 설정
        return message;
    }
}