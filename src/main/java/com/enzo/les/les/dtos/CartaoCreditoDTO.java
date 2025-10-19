package com.enzo.les.les.dtos;

import com.enzo.les.les.enums.BandeiraCartaoEnum;
import com.enzo.les.les.model.entities.CartaoCredito;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CartaoCreditoDTO {

    private Long id;

    @NotBlank(message = "Número do cartão não pode ser vazio")
    @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter exatamente 16 dígitos")
    private String numeroCartao;

    @NotBlank(message = "Nome impresso não pode ser vazio")
    @Size(min = 3, max = 100, message = "Nome impresso deve ter entre 3 e 100 caracteres")
    private String nomeImpresso;

    @NotBlank(message = "Código de segurança não pode ser vazio")
    @Pattern(regexp = "\\d{3,4}", message = "Código de segurança deve ter 3 ou 4 dígitos")
    private String codigoSeguranca;

    @NotNull(message = "Bandeira do cartão não pode ser vazia")
    private BandeiraCartaoEnum bandeira;

    private LocalDate validade;

    @NotNull(message = "Cliente ID não pode ser vazio")
    private Long clienteId;

    public CartaoCredito mapToEntity() {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setId(this.id);
        cartao.setNumeroCartao(this.numeroCartao);
        cartao.setNomeImpresso(this.nomeImpresso);
        cartao.setCodigoSeguranca(this.codigoSeguranca);
        cartao.setBandeira(this.bandeira);
        cartao.setValidade(this.validade);
        return cartao;
    }
}
