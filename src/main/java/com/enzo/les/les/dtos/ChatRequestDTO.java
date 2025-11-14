package com.enzo.les.les.dtos;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRequestDTO {
    @NotNull
    private Long clienteId;

    // O histórico completo da conversa até agora
    private List<ChatMessageDTO> historico; 
}