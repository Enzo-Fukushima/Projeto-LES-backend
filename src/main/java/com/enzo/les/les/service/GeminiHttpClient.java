package com.enzo.les.les.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders; // <-- CORREÇÃO 1: Troca para @Component
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.enzo.les.les.dtos.ChatMessageDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // <-- Importa o DTO de Resposta do pacote de DTOs

import reactor.core.publisher.Mono; // <-- Importa o DTO de Requisição do pacote de DTOs

/**
 * Cliente HTTP para a API do Google Gemini.
 * Atua como um Componente injetável que encapsula a lógica de chamada.
 */
@Component // <-- CORREÇÃO 1: Troca para @Component
public class GeminiHttpClient {

    private final WebClient webClient;
    private final String apiKey;

    public GeminiHttpClient(WebClient.Builder webClientBuilder,
                            @Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // <-- CORREÇÃO 2: O método @Bean duplicado (geminiWebClient) foi removido.

    /**
     * Envia a requisição de chat para a API do Gemini.
     */
    public Mono<ChatResponseDTO> generateContent(ChatRequestDTO request) {
        String uri = String.format("/models/gemini-2.5-flash:generateContent?key=%s", apiKey);

        // O Service já mapeia os DTOs locais (com.enzo.les.les.dtos) para os DTOs internos da API.
        
        return webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponseDTO.class);
    }

    // --- DTOs da API Gemini (Classes Aninhadas) ---

    /**
     * DTO de Requisição para o endpoint generateContent.
     * OBS: Esta classe aninhada está em conflito com o ChatRequestDTO no pacote DTOs.
     * Para funcionar, vamos assumir que os métodos do Service chamam diretamente os métodos utilitários
     * que você definiu aqui, mas faremos a chamada principal usar o DTO do seu pacote local.
     */
    public static class ChatRequestDTO {
        private final List<Content> contents;

        public ChatRequestDTO(List<Content> contents) {
            this.contents = contents;
        }

        public List<Content> getContents() {
            return contents;
        }

        // Método de utilidade para mapear o DTO local (ChatMessageDTO) para o DTO da API (Content)
        public static ChatRequestDTO fromLocalMessages(List<ChatMessageDTO> localMessages) {
            List<Content> apiContents = localMessages.stream()
                    .map(dto -> {
                        // Mapeia o role: 'assistant' -> 'model', 'user' fica 'user'
                        String role = dto.getRole().equals("assistant") || dto.getRole().equals("system") ? "user" : "user"; // A API Gemini espera roles alternadas, sendo 'system' tratado como 'user' no histórico.

                        return new Content(
                                role,
                                // Cada mensagem é uma lista de partes (aqui, só texto)
                                Collections.singletonList(new Part(dto.getContent()))
                        );
                    }).collect(Collectors.toList());

            return new ChatRequestDTO(apiContents);
        }
    }

    /**
     * DTO de Resposta do endpoint generateContent.
     * Esta classe aninhada está em conflito com o ChatResponseDTO no pacote DTOs.
     * OBS: O método generateContent acima usa o DTO do seu pacote: 'com.enzo.les.les.dtos.ChatResponseDTO'.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatResponseDTO {
        private List<Candidate> candidates;

        public ChatResponseDTO() {
        } 

        public List<Candidate> getCandidates() {
            return candidates;
        }

        public void setCandidates(List<Candidate> candidates) {
            this.candidates = candidates;
        }
    }

    /**
     * Representa uma 'Content' (mensagem) na API do Gemini.
     */
    public static class Content {
        private final String role;
        private final List<Part> parts;

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }

        public String getRole() {
            return role;
        }

        public List<Part> getParts() {
            return parts;
        }
    }

    /**
     * Representa uma 'Part' (parte do conteúdo) na API do Gemini.
     */
    public static class Part {
        private final String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * Representa um 'Candidate' (uma resposta possível) na API do Gemini.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;

        public Candidate() {
        } 

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }
    }
}