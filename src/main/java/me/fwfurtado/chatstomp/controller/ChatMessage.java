package me.fwfurtado.chatstomp.controller;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatMessage {

    private final LocalDateTime date = LocalDateTime.now();
    private String content;
}
