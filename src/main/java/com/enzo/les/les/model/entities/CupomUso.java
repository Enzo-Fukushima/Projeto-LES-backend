package com.enzo.les.les.model.entities;

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
@Table(name = "cupom_uso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupomUso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Cupom
    @ManyToOne
    @JoinColumn(name = "cupom_id", nullable = false)
    private Cupom cupom;

    // Relacionamento com Pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Relacionamento com Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
