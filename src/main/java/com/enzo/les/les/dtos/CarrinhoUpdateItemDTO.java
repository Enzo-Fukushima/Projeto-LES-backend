package com.enzo.les.les.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoUpdateItemDTO {
    @NotNull
    private Long carrinhoId;
    @NotNull
    private Long livroId;
    @Min(0)
    private Integer quantidade; // se 0 => remover
}
