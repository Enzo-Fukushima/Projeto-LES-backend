package com.enzo.les.les.controller;

import java.util.List;

import com.enzo.les.les.dtos.*;
import com.enzo.les.les.service.ClienteService;
import com.enzo.les.les.service.EnderecoService;
import com.enzo.les.les.service.CartaoCreditoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final EnderecoService enderecoService;
    private final CartaoCreditoService cartaoCreditoService;

    public ClienteController(ClienteService clienteService,
                             EnderecoService enderecoService,
                             CartaoCreditoService cartaoCreditoService) {
        this.clienteService = clienteService;
        this.enderecoService = enderecoService;
        this.cartaoCreditoService = cartaoCreditoService;
    }

    // ---------------- CLIENTES ----------------
    @Operation(summary = "Listar todas os clientes", description = "Retorna uma lista com todas os clientes cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public List<ClienteDTO> getAllClientes() {
        return clienteService.listarTodos();
    }

    @Operation(summary = "Buscar Cliente por ID", description = "Retorna um cliente com o id selecionado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDetalhadoDTO> getClienteById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Criar um cliente", description = "Cria um cliente no sistema com os dados informados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha ao criar cliente"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ClienteDetalhadoDTO> createCliente(@Valid @RequestBody CreateClienteDTO dto) {
        return ResponseEntity.ok(clienteService.salvar(dto));
    }

    @Operation(summary = "Atualizar cliente", description = "Edita os dados de um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteUpdateDTO> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDTO dto) {
        ClienteUpdateDTO existing = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Atualizar senha do cliente", description = "Edita a senha de um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/senha")
    public ResponseEntity<ClienteDetalhadoDTO> updateClienteSenha(@PathVariable Long id, @Valid @RequestBody ClienteUpdateSenhaDTO dto) {
        ClienteDetalhadoDTO existing = clienteService.alterarSenha(id, dto);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Inativar cliente", description = "Inativa o cadastro de um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarCliente(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar cliente", description = "Ativa o cadastro de um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarCliente(@PathVariable Long id) {
        clienteService.ativarCliente(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- ENDEREÇOS ----------------
    @Operation(summary = "Buscar endereço por ID do cliente", description = "Retorna um endereço com base no ID do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista endereço encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista Endereço não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<EnderecoDTO>> getEnderecoByClienteId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarEnderecosDoUsuario(id));
    }

    @Operation(summary = "Criar endereço", description = "Cadastra um novo endereço")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha ao criar endereço"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/enderecos")
    public ResponseEntity<EnderecoDTO> createEndereco(@Valid @RequestBody EnderecoDTO dto) {
        return ResponseEntity.ok(enderecoService.salvar(dto));
    }

    @Operation(summary = "Atualizar endereço", description = "Edita um endereço existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoDTO> updateEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoDTO dto) {
        return ResponseEntity.ok(enderecoService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar endereço", description = "Remove um endereço pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/enderecos/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- CARTÕES ----------------
    @Operation(summary = "Buscar cartão por ID", description = "Retorna um cartão com base no ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cartão encontrado"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/cartoes/{id}")
    public ResponseEntity<CartaoCreditoDTO> getCartaoById(@PathVariable Long id) {
        return ResponseEntity.ok(cartaoCreditoService.buscarPorId(id));
    }

    @Operation(summary = "Buscar endereço por ID do cliente", description = "Retorna um endereço com base no ID do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista endereço encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista Endereço não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}/cartoes")
    public ResponseEntity<List<CartaoCreditoDTO>> getCartoesByClienteId(@PathVariable Long id) {
        return ResponseEntity.ok(cartaoCreditoService.buscarCartoesPorCliente(id));
    }

    @Operation(summary = "Criar cartão", description = "Cadastra um novo cartão de crédito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cartão criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha ao criar cartão"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/cartoes")
    public ResponseEntity<CartaoCreditoDTO> createCartao(@Valid @RequestBody CartaoCreditoDTO dto) {
        return ResponseEntity.ok(cartaoCreditoService.salvar(dto));
    }

    @Operation(summary = "Deletar cartão", description = "Remove um cartão pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cartão deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/cartoes/{id}")
    public ResponseEntity<Void> deleteCartao(@PathVariable Long id) {
        cartaoCreditoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
