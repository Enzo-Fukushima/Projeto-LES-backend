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
public class SolicitarTrocaDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotEmpty(message = "Deve haver pelo menos um item para troca")
    private List<TrocaItemDTO> itens;

    private String motivoTroca;
}