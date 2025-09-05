package com.enzo.les.les.controller;


import java.util.List;

import com.enzo.les.les.model.dtos.ClienteDTO;
import com.enzo.les.les.service.ClienteService;
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

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todas os clientes", description = "retorna uma lista com todas os clientes cadastrados na API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public List<ClienteDTO> getAllClientes(){
        return clienteService.listarTodos();
    }

    @Operation(summary = "Buscar Cliente por ID", description = "Retorna um cliente com o id selecionado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getPessoaById(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Criar um cliente no sistema", description = "Cria um cliente no sistema, através das informações enviadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
            @ApiResponse(responseCode = "400", description = "Falha ao criar cadstro do cliente")
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> createPessoa(@Valid @RequestBody ClienteDTO dto){
        return ResponseEntity.ok(clienteService.salvar(dto));
    }

    @Operation(summary = "Atualiza os dados de um cliente", description = "Edita as informações de um cliente existente, baseando-se no seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updatePessoa(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto){
        ClienteDTO existing = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Inativar um cliente do sistema", description = "Inativa o cadastro de um cliente do sistema, baseando-se no seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarCliente(@PathVariable Long id){
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar um cliente do sistema", description = "Ativa o cadastro de um cliente do sistema, baseando-se no seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarCliente(@PathVariable Long id){
        clienteService.ativarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
