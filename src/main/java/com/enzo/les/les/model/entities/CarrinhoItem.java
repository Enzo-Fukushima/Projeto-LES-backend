package com.enzo.les.les.model.entities;

import com.enzo.les.les.dtos.CarrinhoItemDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrinho_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Carrinho
    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    // Relacionamento com Livro
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @NotNull
    Long clienteId;

    private Integer quantidade;

    public CarrinhoItemDTO mapToDTO(){
        CarrinhoItemDTO dto = new CarrinhoItemDTO();
        dto.setId(this.id);
        dto.setQuantidade(this.quantidade);
        dto.setLivroId(this.livro.getId());
        dto.setClienteId(this.clienteId);
        return dto;
    }
}
