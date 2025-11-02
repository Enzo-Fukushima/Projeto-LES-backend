package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.CupomDTO;
import com.enzo.les.les.dtos.CupomUseDTO;
import com.enzo.les.les.model.entities.Cupom;
import com.enzo.les.les.service.CupomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cupons")
@Tag(name = "Cupons", description = "API de gerenciamento de cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @Operation(summary = "Validar cupom por código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupom válido"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
            @ApiResponse(responseCode = "400", description = "Cupom inválido ou expirado")
    })
    @GetMapping("/validar/{codigo}")
    public ResponseEntity<CupomUseDTO> validar(@PathVariable String codigo) {
        CupomUseDTO dto = cupomService.validarCupom(codigo);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar todos os cupons (Admin)")
    @GetMapping
    public ResponseEntity<List<CupomDTO>> listarTodos() {
        List<Cupom> cupons = cupomService.getAllCupons();
        List<CupomDTO> dtos = cupons.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar cupom por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CupomDTO> buscarPorId(@PathVariable Long id) {
        return cupomService.buscarPorCodigo(String.valueOf(id))
                .map(cupom -> ResponseEntity.ok(mapToDTO(cupom)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar novo cupom (Admin)")
    @PostMapping
    public ResponseEntity<CupomDTO> criar(@RequestBody CupomDTO dto) {
        Cupom cupom = mapToEntity(dto);
        Cupom saved = cupomService.criarCupom(cupom);
        return new ResponseEntity<>(mapToDTO(saved), HttpStatus.CREATED);
    }

    @Operation(summary = "Desativar cupom (Admin)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        cupomService.desativarCupom(id);
        return ResponseEntity.noContent().build();
    }

    // Mappers
    private CupomDTO mapToDTO(Cupom cupom) {
        CupomDTO dto = new CupomDTO();
        dto.setId(cupom.getId());
        dto.setCodigo(cupom.getCodigo());
        dto.setTipoCupom(cupom.getTipoCupom());
        dto.setValor(cupom.getValor());
        dto.setPercentual(cupom.isPercentual());
        dto.setAtivo(cupom.isAtivo());
        dto.setSingleUse(cupom.isSingleUse());
        dto.setValorMinimo(cupom.getValorMinimo());

        if (cupom.getDataValidade() != null) {
            dto.setDataValidade(cupom.getDataValidade().toString());
        }

        if (cupom.getCliente() != null) {
            dto.setClienteId(cupom.getCliente().getId());
            dto.setNomeCliente(cupom.getCliente().getNome());
        }

        if (cupom.getTroca() != null) {
            dto.setTrocaId(cupom.getTroca().getId());
        }

        return dto;
    }

    private Cupom mapToEntity(CupomDTO dto) {
        Cupom cupom = new Cupom();
        cupom.setCodigo(dto.getCodigo());
        cupom.setTipoCupom(dto.getTipoCupom());
        cupom.setValor(dto.getValor());
        cupom.setPercentual(dto.isPercentual());
        cupom.setAtivo(dto.isAtivo());
        cupom.setSingleUse(dto.isSingleUse());
        cupom.setValorMinimo(dto.getValorMinimo());

        if (dto.getDataValidade() != null) {
            cupom.setDataValidade(java.time.LocalDate.parse(dto.getDataValidade()));
        }

        return cupom;
    }
}