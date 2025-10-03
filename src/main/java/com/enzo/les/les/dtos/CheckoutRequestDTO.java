package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {

    // Opcional se o backend obtém cliente pelo token, mas incluímos para testes
    private Long clienteId;

    @NotNull
    private Long carrinhoId;

    // Entrega: ou ID de endereço existente ou novo endereço
    private Long enderecoEntregaId;
    private EnderecoDTO novoEnderecoEntrega;

    // Formas de pagamento (muitos cartões e/ou cupons)
    private List<PagamentoCartaoDTO> cartoesPagamento;

    // Cupons promocionais / de troca
    private List<CupomUseDTO> cupons;

    // Um cupom promocional único (opcional, pode ser redundante com cupons)
    private String cupomPromocionalCodigo;

    // Valor total enviado (opcional; servidor deve recalcular/validar)
    private BigDecimal valorTotal;

    // Observações, frete, etc.
    private String observacoes;
}
