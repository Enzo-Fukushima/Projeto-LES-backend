package com.enzo.les.les.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

      
        GeminiHttpClient.ChatRequestDTO apiRequest =
                GeminiHttpClient.ChatRequestDTO.fromLocalMessages(historico);

      
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
                .map(l -> "ID:" + l.getId() + " | Título: " + l.getTitulo() + " | Preço: R$" + String.format("%.2f", l.getPreco()))
                .collect(Collectors.toList());

        return String.join("\n", catalogo);
    }

    private String construirPromptSistema(String contexto) {
   
    return String.format("""
        Você é o Chat Concierge do %s. Sua ÚNICA função é recomendar e falar sobre livros.

        REGRAS DE COMPORTAMENTO CRÍTICAS:
        1. Responda sempre em Português e com uma linguagem amigável, como um atendente de livraria.
        2. **FOCO EXCLUSIVO**: Mantenha o foco estrito em livros e recomendações. Se o usuário perguntar sobre tópicos não relacionados (ex: "gosto de praia", notícias, esportes) ou perguntar coisas que não te relação com a livraria, diga educadamente que não pode continuar no tópico e sugira que ele mantenha no tópico de livros.
        3. **USO DO PREÇO**: O catálogo que você recebeu inclui o Título, ID, e **Preço**. Se o usuário perguntar sobre "livro barato" ou "preços", **VOCÊ DEVE** utilizar a informação de preço fornecida para justificar sua recomendação. **NUNCA diga que não tem acesso ao preço.**
        4. os livros recomendados devem ser apenas nome e não devem conter o ID
        5. Use os preços vindos do banco para fazer a recomendação

        CONTEXTO COMPLETO E CATÁLOGO:
        %s
        """, NOME_E_COMMERCE, contexto);
}
}
