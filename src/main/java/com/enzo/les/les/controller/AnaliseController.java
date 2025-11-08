package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.SalesAnalyticsDTO;
import com.enzo.les.les.service.AnaliseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnaliseController {

    private final AnaliseService analiseService;

    public AnaliseController(AnaliseService analiseService) {
        this.analiseService = analiseService;
    }

    @Operation(summary = "Retorna volume total de vendas por período")
    @GetMapping("/vendas/total")
    public ResponseEntity<Double> getVolumeVendasTotal(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        Double total = analiseService.getAnaliseVendas(inicio, fim);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Retorna análise de vendas diária por produto ou categoria")
    @GetMapping("/vendas")
    public ResponseEntity<List<SalesAnalyticsDTO>> getVendasDetalhadas(
            @RequestParam("tipo") String tipo,
            @RequestParam("id") Long id,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        List<SalesAnalyticsDTO> dados;

        if ("PRODUTO".equalsIgnoreCase(tipo)) {
            dados = analiseService.getVendasPorProduto(id, dataInicio, dataFim);
        } else if ("CATEGORIA".equalsIgnoreCase(tipo)) {
            dados = analiseService.getVendasPorCategoria(id, dataInicio, dataFim);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(dados);
    }
}