package com.enzo.les.les.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoCartaoDTO {
    // Use um dos dois: cartaoId (cartão cadastrado) ou newCard (dados do cartão novo)
    private Long cartaoId;
    private NovoCardDTO newCard;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal valor; // quanto desse pagamento será coberto por este cartão

}
