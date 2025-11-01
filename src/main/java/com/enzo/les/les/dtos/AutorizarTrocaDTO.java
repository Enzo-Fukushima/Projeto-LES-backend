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
public class AutorizarTrocaDTO {

    @NotNull(message = "ID da troca é obrigatório")
    private Long trocaId;

    private Boolean autorizada;

    private String observacao;
}