package com.enzo.les.les.model.entities;

import com.enzo.les.les.dtos.CarrinhoDTO;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoItem> itens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private Cupom cupom;

    @Column
    private BigDecimal desconto = BigDecimal.ZERO;

    public CarrinhoDTO mapToDTO(){
        CarrinhoDTO dto = new CarrinhoDTO();
        dto.setId(this.id);
        dto.setClienteId(this.cliente.getId());
        dto.setDesconto(this.desconto);
        dto.setCupomCodigo(this.cupom != null ? this.cupom.getCodigo() : null);
        dto.setItens(this.itens.stream().map(CarrinhoItem::mapToDTO).toList());
        return dto;
    }
}
