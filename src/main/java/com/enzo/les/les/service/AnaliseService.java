package com.enzo.les.les.service;

import com.enzo.les.les.dtos.SalesAnalyticsDTO;
import com.enzo.les.les.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnaliseService {

    private final PedidoRepository pedidoRepository;

    public AnaliseService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Double getAnaliseVendas(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.getVolumeVendasPorPeriodo(
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );
    }

    public List<SalesAnalyticsDTO> getVendasPorProduto(Long produtoId, LocalDate inicio, LocalDate fim) {
        List<SalesAnalyticsDTO> dados = pedidoRepository.getVendasPorProduto(produtoId, inicio, fim);
        return preencherDatasVazias(dados, inicio, fim);
    }

    public List<SalesAnalyticsDTO> getVendasPorCategoria(Long categoriaId, LocalDate inicio, LocalDate fim) {
        List<SalesAnalyticsDTO> dados = pedidoRepository.getVendasPorCategoria(categoriaId, inicio, fim);
        return preencherDatasVazias(dados, inicio, fim);
    }

    /**
     * Preenche os dias que não tiveram vendas com quantidade=0 e valorTotal=0
     * para que o gráfico mostre uma linha contínua
     */
    private List<SalesAnalyticsDTO> preencherDatasVazias(
            List<SalesAnalyticsDTO> dados,
            LocalDate inicio,
            LocalDate fim
    ) {
        Map<LocalDate, SalesAnalyticsDTO> mapaVendas = dados.stream()
                .collect(Collectors.toMap(SalesAnalyticsDTO::getData, dto -> dto));

        List<SalesAnalyticsDTO> resultado = new ArrayList<>();

        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            SalesAnalyticsDTO dto = mapaVendas.getOrDefault(
                    dataAtual,
                    new SalesAnalyticsDTO(dataAtual, 0L, BigDecimal.ZERO)  // ✅ 0L ao invés de 0
            );
            resultado.add(dto);
            dataAtual = dataAtual.plusDays(1);
        }

        return resultado;
    }
}