package com.enzo.les.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrocaDTO {
    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private String nomeCliente;
    private String status;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAutorizacao;
    private LocalDateTime dataRecebimento;
    private LocalDateTime dataConclusao;
    private String motivoTroca;
    private String observacaoAdmin;
    private BigDecimal valorTotalTroca;
    private List<TrocaItemDTO> itens;
    private String codigoCupom;
}