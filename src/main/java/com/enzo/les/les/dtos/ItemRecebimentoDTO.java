package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRecebimentoDTO {

    @NotNull(message = "ID do item da troca é obrigatório")
    private Long trocaItemId;

    @NotNull(message = "Deve informar se retorna ao estoque")
    private Boolean retornarEstoque;
}