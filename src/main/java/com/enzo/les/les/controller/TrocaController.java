package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.*;
import com.enzo.les.les.service.TrocaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trocas")
@Tag(name = "Trocas", description = "API de gerenciamento de trocas de produtos")
public class TrocaController {

    @Autowired
    private TrocaService trocaService;

    /**
     * 1. SOLICITAR TROCA (Cliente)
     */
    @Operation(summary = "Solicitar troca de itens",
            description = "Permite que o cliente solicite a troca de itens de um pedido entregue")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Troca solicitada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou pedido não elegível para troca"),
            @ApiResponse(responseCode = "404", description = "Pedido ou cliente não encontrado")
    })
    @PostMapping
    public ResponseEntity<TrocaDTO> solicitarTroca(@Valid @RequestBody SolicitarTrocaDTO dto) {
        TrocaDTO troca = trocaService.solicitarTroca(dto);
        return new ResponseEntity<>(troca, HttpStatus.CREATED);
    }

    /**
     * 2. AUTORIZAR TROCA (Admin)
     */
    @Operation(summary = "Autorizar ou negar troca",
            description = "Permite que o administrador autorize ou negue uma solicitação de troca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Troca processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status da troca não permite autorização"),
            @ApiResponse(responseCode = "404", description = "Troca não encontrada")
    })
    @PutMapping("/autorizar")
    public ResponseEntity<TrocaDTO> autorizarTroca(@Valid @RequestBody AutorizarTrocaDTO dto) {
        TrocaDTO troca = trocaService.autorizarTroca(dto);
        return ResponseEntity.ok(troca);
    }

    /**
     * 3. LISTAR TROCAS PENDENTES (Admin)
     */
    @Operation(summary = "Listar trocas pendentes",
            description = "Retorna todas as trocas com status EM_TROCA aguardando autorização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/pendentes")
    public ResponseEntity<List<TrocaDTO>> listarTrocasPendentes() {
        List<TrocaDTO> trocas = trocaService.listarTrocasPendentes();
        return ResponseEntity.ok(trocas);
    }

    /**
     * 4. CONFIRMAR RECEBIMENTO (Admin)
     */
    @Operation(summary = "Confirmar recebimento dos itens",
            description = "Confirma o recebimento dos itens trocados e gera o cupom de troca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recebimento confirmado e cupom gerado"),
            @ApiResponse(responseCode = "400", description = "Status da troca não permite confirmação"),
            @ApiResponse(responseCode = "404", description = "Troca não encontrada")
    })
    @PutMapping("/confirmar-recebimento")
    public ResponseEntity<TrocaDTO> confirmarRecebimento(@Valid @RequestBody ConfirmarRecebimentoDTO dto) {
        TrocaDTO troca = trocaService.confirmarRecebimento(dto);
        return ResponseEntity.ok(troca);
    }

    /**
     * 5. BUSCAR TROCA POR ID
     */
    @Operation(summary = "Buscar troca por ID",
            description = "Retorna os detalhes de uma troca específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Troca encontrada"),
            @ApiResponse(responseCode = "404", description = "Troca não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrocaDTO> buscarPorId(@PathVariable Long id) {
        TrocaDTO troca = trocaService.buscarPorId(id);
        return ResponseEntity.ok(troca);
    }

    /**
     * 6. LISTAR TROCAS DO CLIENTE
     */
    @Operation(summary = "Listar trocas de um cliente",
            description = "Retorna todas as trocas realizadas por um cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TrocaDTO>> listarTrocasCliente(@PathVariable Long clienteId) {
        List<TrocaDTO> trocas = trocaService.listarTrocasCliente(clienteId);
        return ResponseEntity.ok(trocas);
    }

    /**
     * 7. LISTAR TODAS AS TROCAS (Admin)
     */
    @Operation(summary = "Listar todas as trocas",
            description = "Retorna todas as trocas do sistema (administrador)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<TrocaDTO>> listarTodasTrocas() {
        List<TrocaDTO> trocas = trocaService.listarTodasTrocas();
        return ResponseEntity.ok(trocas);
    }

    /**
     * 8. LISTAR TROCAS POR STATUS
     */
    @Operation(summary = "Listar trocas por status",
            description = "Retorna trocas filtradas por status (EM_TROCA, TROCA_AUTORIZADA, etc)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrocaDTO>> listarPorStatus(@PathVariable String status) {
        List<TrocaDTO> trocas = trocaService.listarTrocasPorStatus(status);
        return ResponseEntity.ok(trocas);
    }
}