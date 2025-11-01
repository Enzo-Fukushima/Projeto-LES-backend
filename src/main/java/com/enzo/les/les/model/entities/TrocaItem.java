package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "troca_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrocaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "troca_id", nullable = false)
    private Troca troca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_item_id", nullable = false)
    private PedidoItem pedidoItem;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "retornar_estoque", nullable = false)
    private Boolean retornarEstoque = false;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    public void calcularSubtotal() {
        this.subtotal = this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }
}