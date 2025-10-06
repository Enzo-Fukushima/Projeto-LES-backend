package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.CupomUseDTO;
import com.enzo.les.les.service.CupomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @Operation(summary = "Validar cupom por código")
    @GetMapping("/{codigo}")
    public ResponseEntity<CupomUseDTO> validar(@PathVariable String codigo) {
        CupomUseDTO dto = cupomService.validarCupom(codigo);
        return ResponseEntity.ok(dto);
    }
}
