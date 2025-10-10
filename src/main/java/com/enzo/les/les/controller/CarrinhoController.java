package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
import com.enzo.les.les.dtos.CarrinhoUpdateItemDTO;
import com.enzo.les.les.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @Operation(summary = "Buscar carrinho do cliente", description = "Retorna o carrinho atual do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrinho retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado")
    })
    @GetMapping("cliente/{clienteId}")
    public ResponseEntity<CarrinhoDTO> getCarrinhoByCliente(@PathVariable Long clienteId) {
    return carrinhoService.buscarCarrinhoPorCliente(clienteId)
            .map(ResponseEntity::ok)                  // 200 OK com o DTO
            .orElseGet(() -> ResponseEntity.notFound().build()); // 404 se não encontrado
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<CarrinhoDTO> criarCarrinho(@PathVariable Long clienteId) {
        return ResponseEntity.ok(carrinhoService.criarCarrinho(clienteId));
    }

    @PostMapping("/{carrinhoId}/itens")
    public ResponseEntity<CarrinhoDTO> adicionarItem(@PathVariable Long carrinhoId, @RequestBody @Valid CarrinhoItemDTO dto) {
        return ResponseEntity.ok(carrinhoService.adicionarItem(carrinhoId, dto));
    }

    @Operation(summary = "Atualizar quantidade de item do carrinho")
    @PutMapping("/{carrinhoId}/itens")
    public ResponseEntity<CarrinhoDTO> updateItem(@PathVariable Long carrinhoId,@Valid @RequestBody CarrinhoUpdateItemDTO dto) {
        return ResponseEntity.ok(carrinhoService.atualizarQuantidade(carrinhoId, dto));
    }

    @DeleteMapping("/{carrinhoId}/itens/{livroId}")
    public ResponseEntity<CarrinhoDTO> removeItem(@PathVariable Long carrinhoId, @PathVariable Long livroId) {
        return ResponseEntity.ok(carrinhoService.removerItem(carrinhoId, livroId));
    }

}
