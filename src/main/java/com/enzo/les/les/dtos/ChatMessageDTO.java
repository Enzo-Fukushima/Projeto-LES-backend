package com.enzo.les.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    // "user" para o cliente, "model" para a IA
    private String role; 
    private String content;
}