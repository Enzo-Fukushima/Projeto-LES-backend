package com.enzo.les.les.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoDTO {
    private Long id;
    @NotNull
    private Long clienteId;
    private List<CarrinhoItemDTO> itens;
    private BigDecimal desconto;
    private String cupomCodigo;
}
