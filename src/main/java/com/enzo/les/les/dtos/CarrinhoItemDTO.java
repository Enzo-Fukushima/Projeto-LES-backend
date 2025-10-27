package com.enzo.les.les.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
