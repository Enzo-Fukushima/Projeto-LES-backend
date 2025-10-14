package com.enzo.les.les.model.entities;

import com.enzo.les.les.enums.BandeiraCartaoEnum;
import com.enzo.les.les.dtos.CartaoCreditoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cartoes_credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cartao", nullable = false, length = 16, unique = true)
    private String numeroCartao;

    @Column(name = "nome_impresso", nullable = false)
    private String nomeImpresso;

    @Column(name = "codigo_seguranca", nullable = false, length = 4)
    private String codigoSeguranca;

    private BandeiraCartaoEnum bandeira;

    @Column(name = "data_validade", nullable = false)
    private LocalDate validade;

    // Relacionamento com Cliente (um cliente pode ter vários cartões)
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    public CartaoCreditoDTO mapToDTO() {
        CartaoCreditoDTO dto = new CartaoCreditoDTO();
        dto.setId(this.id);
        dto.setNumeroCartao(this.numeroCartao);
        dto.setNomeImpresso(this.nomeImpresso);
        dto.setCodigoSeguranca(this.codigoSeguranca);
        dto.setBandeira(this.bandeira);
        dto.setClienteId(this.cliente != null ? this.cliente.getId() : null);
        return dto;
    }

    public void update(CartaoCreditoDTO dto){
        this.setBandeira(dto.getBandeira());
        this.setNumeroCartao(dto.getNumeroCartao());
        this.setNomeImpresso(dto.getNomeImpresso());
        this.setCodigoSeguranca(dto.getCodigoSeguranca());
    }
}
