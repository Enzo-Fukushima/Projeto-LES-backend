package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmarRecebimentoDTO {

    @NotNull(message = "ID da troca é obrigatório")
    private Long trocaId;

    @NotEmpty(message = "Deve informar quais itens retornam ao estoque")
    private List<ItemRecebimentoDTO> itens;

    private String observacao;
}