package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.CheckoutRequestDTO;
import com.enzo.les.les.dtos.CheckoutResponseDTO;
import com.enzo.les.les.dtos.OrderDTO;
import com.enzo.les.les.model.entities.Pedido;
import com.enzo.les.les.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Criar pedido (checkout)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout(@Valid @RequestBody CheckoutRequestDTO dto) {
        CheckoutResponseDTO resp = pedidoService.criarPedidoAPartirDoCarrinho(dto);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getPedido(@PathVariable Long id) {
        OrderDTO dto = pedidoService.consultarPedido(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar pedidos de um cliente pelo id do cliente")
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<OrderDTO>> getPedidosByCliente(@PathVariable Long id){
        List<OrderDTO> listDto = pedidoService.getPedidosByClienteId(id);
        return ResponseEntity.ok(listDto);
    }

    @Operation(summary = "Buscar todos os pedidos do sistema")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllPedidos(){
        List<OrderDTO> listDto = pedidoService.getAllPedidos();
        return ResponseEntity.ok(listDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> payload) {

        String status = payload.get("status");
        Pedido pedido = pedidoService.updateStatus(id, status);
        return ResponseEntity.ok(pedido.mapToDTO());
    }
}
