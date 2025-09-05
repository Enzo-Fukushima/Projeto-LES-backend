package com.enzo.les.les.model.entities;

import com.enzo.les.les.enums.BandeiraCartaoEnum;
import jakarta.persistence.*;
import lombok.*;

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

    // Relacionamento com Bandeira (RN0025)
    @ManyToOne
    @JoinColumn(name = "bandeira_id", nullable = false)
    private BandeiraCartaoEnum bandeira;

    // Relacionamento com Cliente (um cliente pode ter vários cartões)
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
