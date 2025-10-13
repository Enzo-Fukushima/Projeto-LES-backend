package com.enzo.les.les.model.entities;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
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

    public CarrinhoDTO mapToDTO() {
        CarrinhoDTO dto = new CarrinhoDTO();
        dto.setId(this.id);
        dto.setClienteId(this.cliente.getId());
        dto.setDesconto(this.desconto);
        dto.setCupomCodigo(this.cupom != null ? this.cupom.getCodigo() : null);

        List<CarrinhoItemDTO> itensDTO = this.itens.stream().map(item -> {
            CarrinhoItemDTO itemDTO = new CarrinhoItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setClienteId(this.cliente.getId());
            itemDTO.setLivroId(item.getLivro().getId());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setTitulo(item.getLivro().getTitulo());
            itemDTO.setPrecoUnitario(BigDecimal.valueOf(item.getLivro().getPreco()));
            return itemDTO;
        }).toList();

        dto.setItens(itensDTO);

        return dto;
    }
}
