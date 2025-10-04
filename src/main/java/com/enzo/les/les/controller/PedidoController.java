package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.CheckoutRequestDTO;
import com.enzo.les.les.dtos.CheckoutResponseDTO;
import com.enzo.les.les.dtos.OrderDTO;
import com.enzo.les.les.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
