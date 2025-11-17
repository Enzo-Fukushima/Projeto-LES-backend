package com.enzo.les.les.service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enzo.les.les.dtos.ChatMessageDTO;
import com.enzo.les.les.dtos.ChatRequestDTO;
import com.enzo.les.les.dtos.ChatResponseDTO;
import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.LivroRepository;

@Service
public class RecomendacaoChatService {

    private final LivroRepository livroRepository;
    private final GeminiHttpClient chatClient;

    private static final Pattern BOOK_PATTERN = Pattern.compile("\\[BOOK:(\\d+)]");
    private static final String NOME_E_COMMERCE = "Livruvru: E-commerce de Livros";

    @Autowired
    public RecomendacaoChatService(
            LivroRepository livroRepository,
            GeminiHttpClient chatClient) {
        this.livroRepository = livroRepository;
        this.chatClient = chatClient;
    }

    public ChatResponseDTO gerarRespostaChat(ChatRequestDTO request) {

        // SYSTEM + histórico
        List<ChatMessageDTO> historico = new ArrayList<>();
        historico.add(new ChatMessageDTO("system", construirPromptSistema(construirContextoDoCliente(request.getClienteId()))));
        historico.addAll(request.getHistorico());

        // Mapeia para GeminiRequest
        GeminiHttpClient.ChatRequestDTO apiRequest =
                GeminiHttpClient.ChatRequestDTO.fromLocalMessages(historico);

        // CHAMADA SINCRONA
        GeminiHttpClient.ChatResponseDTO apiResponse =
                chatClient.generateContent(apiRequest);

        return processarRespostaIA(apiResponse);
    }

    private ChatResponseDTO processarRespostaIA(GeminiHttpClient.ChatResponseDTO apiResponse) {

        String respostaTexto = Optional.ofNullable(apiResponse.getCandidates())
                .flatMap(list -> list.stream().findFirst())
                .map(candidate -> candidate.getContent().getParts().get(0).getText())
                .orElse("Não foi possível gerar uma resposta.");

        Set<Long> livroIds = new HashSet<>();

        Matcher m = BOOK_PATTERN.matcher(respostaTexto);
        while (m.find()) {
            try {
                livroIds.add(Long.valueOf(m.group(1)));
            } catch (Exception ignored) {}
        }

        List<Livro> livros = livroRepository.findAllByIdInAndEstoqueGreaterThanZero(livroIds);

        List<LivroDTO> recomendados = livros.stream()
                .map(Livro::mapToDTO)
                .collect(Collectors.toList());

        ChatResponseDTO resp = new ChatResponseDTO();
        resp.setRespostaIA(new ChatMessageDTO("assistant", respostaTexto));
        resp.setLivrosRecomendados(recomendados);

        return resp;
    }

    private String construirContextoDoCliente(Long clienteId) {
        String perfilCliente = "Cliente ID " + clienteId;
        List<String> catalogo = livroRepository.findTop20MaisVendidos()
                .stream()
                .map(l -> "ID:" + l.getId() + " | " + l.getTitulo())
                .collect(Collectors.toList());

        return String.join("\n", catalogo);
    }

    private String construirPromptSistema(String contexto) {
        return String.format("""
                Você é o Chat Concierge do %s.
                Regras:
                - Sempre responda em Português.
                - Quando citar um livro, use [BOOK:ID].
                Catálogo:
                %s
                """, NOME_E_COMMERCE, contexto);
    }
}
