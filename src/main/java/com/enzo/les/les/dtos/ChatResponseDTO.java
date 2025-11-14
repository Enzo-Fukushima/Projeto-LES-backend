// DTO: com.enzo.les.les.dtos.ChatResponseDTO.java

package com.enzo.les.les.dtos;

import java.util.List;

public class ChatResponseDTO {
    
    private ChatMessageDTO respostaIA;
    private List<LivroDTO> livrosRecomendados;
    
    // Construtor padrão (necessário para Jackson)
    public ChatResponseDTO() {
    }

    // MÉTODO QUE ESTAVA FALTANDO/DANDO ERRO (SETTER)
    public void setRespostaIA(ChatMessageDTO respostaIA) {
        this.respostaIA = respostaIA;
    }

    // MÉTODO QUE ESTAVA FALTANDO/DANDO ERRO (SETTER)
    public void setLivrosRecomendados(List<LivroDTO> livrosRecomendados) {
        this.livrosRecomendados = livrosRecomendados;
    }
    
    // Getters
    public ChatMessageDTO getRespostaIA() {
        return respostaIA;
    }

    public List<LivroDTO> getLivrosRecomendados() {
        return livrosRecomendados;
    }
}