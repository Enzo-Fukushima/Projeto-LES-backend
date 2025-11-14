package com.enzo.les.les.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enzo.les.les.dtos.ChatMessageDTO;
import com.enzo.les.les.dtos.ChatRequestDTO;
import com.enzo.les.les.dtos.ChatResponseDTO;
import com.enzo.les.les.service.RecomendacaoChatService;

/**
 * Controller REST para o Chatbot de Recomendação.
 */
@RestController
@RequestMapping("/api/chat")
public class RecomendacaoChatController {

    private final RecomendacaoChatService chatService;

    @Autowired
    public RecomendacaoChatController(RecomendacaoChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Endpoint POST para receber a mensagem do usuário e retornar a resposta da IA junto com os Livros DTOs.
     * @param request Contém o ID do Cliente e o histórico de conversação.
     * @return ChatResponseDTO contendo o texto da IA e os livros recomendados.
     */
    @PostMapping("/recomendacoes")
    public ResponseEntity<ChatResponseDTO> gerarRecomendacoes(@RequestBody ChatRequestDTO request) {
        
        // Verifica se o ID do cliente é válido, um requisito de arquitetura
        if (request.getClienteId() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Chamada síncrona (ChatService agora usa .block())
            ChatResponseDTO response = chatService.gerarRespostaChat(request);
            
            // Verifica se houve um erro no Service (ex: erro de API tratado)
            if (response.getRespostaIA() == null || response.getRespostaIA().getContent().contains("problema de comunicação")) {
                 return ResponseEntity.internalServerError().body(response);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro fatal na requisição do chat: " + e.getMessage());
            // Retorna um erro interno robusto
            ChatResponseDTO errorResponse = new ChatResponseDTO();
            // Garante que o DTO de resposta da IA não seja nulo
            errorResponse.setRespostaIA(new ChatMessageDTO("assistant", "Desculpe, meu sistema de recomendação está indisponível no momento.")); 
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}