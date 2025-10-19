package com.enzo.les.les.model.entities;

import com.enzo.les.les.dtos.OrderItemDTO;
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

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Relacionamento com Livro
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    private BigDecimal precoUnitario;

    private Integer quantidade;

    private BigDecimal subtotal;

    public OrderItemDTO mapToDto(){
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(this.getId());
        dto.setSubtotal(this.getSubtotal());
        dto.setPrecoUnitario(this.getPrecoUnitario());
        dto.setQuantidade(this.getQuantidade());
        return dto;
    }
}
