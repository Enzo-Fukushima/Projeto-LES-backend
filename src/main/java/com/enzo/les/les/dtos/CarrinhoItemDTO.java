package com.enzo.les.les.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItemDTO {

    // ID do item no carrinho (opcional em inserção)
    private Long id;

    private Long clienteId;
    @NotNull
    private long livroId;

    @Min(1)
    private Integer quantidade;

    private String titulo;
    private BigDecimal precoUnitario;
}
