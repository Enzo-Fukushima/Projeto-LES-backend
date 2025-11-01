package com.enzo.les.les.model.entities;

import com.enzo.les.les.enums.TipoCupomEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "tipo_cupom", length = 50)
    private TipoCupomEnum tipoCupom; // TROCA, PROMOCIONAL, PRIMEIRA_COMPRA

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private boolean percentual;

    @Column(name = "valor_minimo")
    private Double valorMinimo;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "single_use", nullable = false)
    private boolean singleUse = false;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // NOVO: Relacionamento com Troca
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "troca_id")
    private Troca troca;
}