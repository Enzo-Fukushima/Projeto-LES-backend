package com.enzo.les.les.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos_troca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoTroca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pedido original
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Item do pedido que foi trocado
    @ManyToOne
    @JoinColumn(name = "item_pedido_id")
    private PedidoItem itemPedido;

    // Cliente que solicitou a troca
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Campo extra para status
    @Column(length = 50)
    private String status; // ex: SOLICITADA, APROVADA, REJEITADA
}
