package com.enzo.les.les.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItemDTO {

    // ID do item no carrinho (opcional em inserção)
    private Long id;

    @NotNull
    private Long livroId;

    @Min(1)
    private Integer quantidade;

    @NotNull(message = "O ID do cliente é obrigatório para criar o carrinho")
    private Long clienteId;
}
