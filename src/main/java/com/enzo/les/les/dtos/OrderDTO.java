package com.enzo.les.les.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long clienteId;
    private EnderecoDTO enderecoEntrega;
    private List<OrderItemDTO> itens;
    private List<CupomUseDTO> cupons;
    private BigDecimal valorTotal;
    private String status; // ABERTO, FECHADO, CANCELADO, ...
    private LocalDateTime dataCriacao;
    private String codigoRastreamento;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataEntrega;
    private LocalDateTime dataPedido; // era dataCriacao
    private String clienteNome; // para exibir no front
}
