package com.enzo.les.les.service;

import java.util.ArrayList; // <-- DTOs de Request vêm do pacote DTOs
import java.util.List; // <-- DTOs de Response vêm do pacote DTOs
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors; // <-- DTOs internos da API

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enzo.les.les.dtos.ChatMessageDTO;
import com.enzo.les.les.dtos.ChatRequestDTO;
import com.enzo.les.les.dtos.ChatResponseDTO;
import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.LivroRepository;
import com.enzo.les.les.service.GeminiHttpClient.Content;

import reactor.core.publisher.Mono;

@Service
public class RecomendacaoChatService {

    private final LivroRepository livroRepository;
    private final GeminiHttpClient chatClient;
    
    private static final Pattern BOOK_PATTERN = Pattern.compile("\\[BOOK:(\\d+)\\]");
    private static final String NOME_E_COMMERCE = "Livruvru: E-commerce de Livros";

    @Autowired
    public RecomendacaoChatService(
            LivroRepository livroRepository,
            GeminiHttpClient chatClient) {
        this.livroRepository = livroRepository;
        this.chatClient = chatClient;
    }

    /**
     * Gera a resposta do chat (SÍNCRONA para o Controller)
     * * @param request Contém o ID do Cliente e o histórico de conversação.
     * @return ChatResponseDTO contendo o texto da IA e os livros filtrados.
     */
    public ChatResponseDTO gerarRespostaChat(ChatRequestDTO request) {
        
        // 1. Contexto (Cliente + Compras + Catálogo)
        String contexto = construirContextoDoCliente(request.getClienteId()); 
        
        // 2. Prompt do Sistema (Persona + Regras)
        String systemPrompt = construirPromptSistema(contexto);
        
        // Adiciona a instrução do sistema como primeira mensagem para guiar a IA
        List<ChatMessageDTO> historicoComSistema = new ArrayList<>();
        // NOTE: Mapeamos o "system" para "user" no ChatRequestDTO.fromLocalMessages()
        historicoComSistema.add(new ChatMessageDTO("system", systemPrompt));
        historicoComSistema.addAll(request.getHistorico());
        
        // 3. Monta a Requisição (usando o DTO interno do GeminiHttpClient)
        GeminiHttpClient.ChatRequestDTO apiRequest = 
            GeminiHttpClient.ChatRequestDTO.fromLocalMessages(historicoComSistema);
        
        // 4. Chamada à IA (usando block() para converter Mono para resposta síncrona)
        Mono<GeminiHttpClient.ChatResponseDTO> monoResponse = chatClient.generateContent(apiRequest);
        
        // Trata erros da requisição HTTP (ex: 400, 500)
        GeminiHttpClient.ChatResponseDTO apiResponse = monoResponse
            .onErrorResume(e -> {
                System.err.println("Erro na chamada à API Gemini: " + e.getMessage());
                return Mono.just(new GeminiHttpClient.ChatResponseDTO()); // Retorna DTO vazio
            })
            .block(); // BLOQUEIA o thread para o Controller Síncrono

        // 5. Processa a resposta da API e retorna o DTO final
        return processarRespostaIA(apiResponse);
    }

    // --- Métodos Auxiliares (mantidos) ---

    private ChatResponseDTO processarRespostaIA(GeminiHttpClient.ChatResponseDTO apiResponse) {
        // Assume que o primeiro candidato é a resposta mais relevante
        String respostaTexto = Optional.ofNullable(apiResponse.getCandidates())
                .flatMap(candidates -> candidates.stream().findFirst())
                .map(candidate -> Optional.ofNullable(candidate.getContent()).orElse(new Content("model", List.of())).getParts().get(0).getText())
                .orElse("Não foi possível gerar uma resposta.");
        
        Matcher matcher = BOOK_PATTERN.matcher(respostaTexto);
        Set<Long> livroIds = new java.util.HashSet<>();
        
        // 1. Extrai IDs do texto
        while (matcher.find()) {
            try {
                livroIds.add(Long.parseLong(matcher.group(1))); 
            } catch (NumberFormatException e) {
                // Ignora
            }
        }
        
        // 2. Busca os Livros que estão em estoque
        // O método findTop20MaisVendidos() deve retornar a lista de livros reais.
        List<Livro> livrosEmEstoque = livroRepository.findAllByIdInAndEstoqueGreaterThanZero(livroIds);

        // 3. Mapeia para o DTO de Livro e monta a resposta final
        List<LivroDTO> livrosRecomendados = livrosEmEstoque.stream()
                .map(Livro::mapToDTO) // Usa o método de mapeamento da entidade Livro
                .collect(Collectors.toList());

        ChatMessageDTO iaMessage = new ChatMessageDTO("assistant", respostaTexto);
        
        // Usa o DTO de resposta do pacote DTOs
        ChatResponseDTO response = new ChatResponseDTO();
        response.setRespostaIA(iaMessage); // Setters funcionam no DTO corrigido
        response.setLivrosRecomendados(livrosRecomendados);
        
        return response;
    }
    
    private String construirContextoDoCliente(Long clienteId) {
        // ... (lógica mantida)
        String perfilCliente = String.format("Cliente ID %d. Status: Não Logado/Novo.", clienteId);
        String historicoDeCompras = "Histórico de Compras: Nenhum.";
        String itensCarrinho = "Itens Atuais no Carrinho: Vazio.";
        
        List<String> catalogoBase = livroRepository.findTop20MaisVendidos()
            .stream()
            .map(l -> String.format("ID:%d | Título: %s", l.getId(), l.getTitulo()))
            .collect(Collectors.toList()); 
        
        return String.format(
            "--- CONTEXTO DO USUÁRIO ---\n" +
            "PERFIL: %s\n" +
            "COMPRAS: %s\n" +
            "CARRINHO: %s\n" +
            "--- CATÁLOGO BASE (%s) ---\n%s\n" +
            "--------------------------\n",
            perfilCliente, historicoDeCompras, itensCarrinho, NOME_E_COMMERCE, String.join("\n", catalogoBase)
        );
    }
    
    private String construirPromptSistema(String contexto) {
        return String.format(
            "Você é o Chat Concierge do %s. Sua função é recomendar livros com base no histórico e preferências do usuário.\n" +
            "REGRAS DE FORMATAÇÃO CRÍTICAS:\n" +
            "1. Sempre responda em Português.\n" +
            "2. Utilize uma linguagem amigável, como um atendente de livraria.\n" +
            "3. Quando você recomendar um livro do catálogo, VOCÊ DEVE INCLUIR o ID do livro imediatamente após a menção, usando o formato EXATO: [BOOK:ID], onde ID é o número do livro. (Ex: 'Você pode gostar de O Pequeno Príncipe [BOOK:42]').\n" +
            "4. Sua resposta DEVE ser apenas o texto de conversação, com as tags de livro incluídas.\n" +
            "5. Não use as tags [BOOK:ID] para IDs que não estão no Catálogo Base fornecido.\n" +
            "CONTEXTO DE DADOS: %s",
            NOME_E_COMMERCE, contexto
        );
    }
}