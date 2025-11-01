package com.enzo.les.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrocaItemDTO {
    private Long id;
    private Long pedidoItemId;
    private Long livroId;
    private String tituloLivro;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
    private Boolean retornarEstoque;
    private String motivo;
}