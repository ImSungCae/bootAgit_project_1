package com.bootagit_project_1.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String roomId;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}

