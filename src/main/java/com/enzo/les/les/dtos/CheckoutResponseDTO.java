package com.enzo.les.les.dtos;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDTO {
    private Long pedidoId;
    private String status; // EX: ABERTO
    private BigDecimal valorTotal;
    private String mensagem;
}
