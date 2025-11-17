package com.enzo.les.les.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.enzo.les.les.dtos.ChatMessageDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeminiHttpClient {

    private final RestClient restClient;
    private final String apiKey;

    public GeminiHttpClient(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ChatResponseDTO generateContent(ChatRequestDTO request) {
        String uri = String.format("/models/gemini-2.5-flash:generateContent?key=%s", apiKey);

        return restClient.post()
                .uri(uri)
                .body(request)
                .retrieve()
                .body(ChatResponseDTO.class);
    }

    // --- DTOs internos da API Gemini ---

    public static class ChatRequestDTO {
        private final List<Content> contents;

        public ChatRequestDTO(List<Content> contents) {
            this.contents = contents;
        }

        public List<Content> getContents() {
            return contents;
        }

        public static ChatRequestDTO fromLocalMessages(List<ChatMessageDTO> messages) {
            List<Content> mapped = messages.stream()
                    .map(msg -> new Content(
                            "user",
                            List.of(new Part(msg.getContent()))
                    ))
                    .collect(Collectors.toList());

            return new ChatRequestDTO(mapped);
        }
    }

    public static class ChatResponseDTO {
        private List<Candidate> candidates;

        public List<Candidate> getCandidates() { return candidates; }
        public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
    }

    public static class Content {
        private String role;
        private List<Part> parts;

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }

        public String getRole() { return role; }
        public List<Part> getParts() { return parts; }
    }

    public static class Part {
        private final String text;

        public Part(String text) { this.text = text; }
        public String getText() { return text; }
    }

    public static class Candidate {
        private Content content;

        public Content getContent() { return content; }
        public void setContent(Content content) { this.content = content; }
    }
}
