package com.enzo.les.les.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.enzo.les.les.dtos.EnderecoDTO;
import com.enzo.les.les.dtos.OrderDTO;
import com.enzo.les.les.dtos.OrderItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente que fez o pedido
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Endereço de entrega
    @ManyToOne
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private Endereco enderecoEntrega;

    // Itens do pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> itens;

    @Column(nullable = false)
    private String status;


    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CupomUso> cuponsUsados;

    @PrePersist
    public void prePersist() {
        this.dataPedido = LocalDateTime.now();
        if (this.status == null) {
            this.status = "ABERTO";
        }
    }

    public OrderDTO mapToDTO() {
        OrderDTO dto = new OrderDTO();
        dto.setId(this.getId());
        dto.setClienteId(this.getCliente().getId());
        dto.setStatus(this.getStatus());
        dto.setDataCriacao(this.getDataPedido());

        // Mapear itens
        List<OrderItemDTO> itens = this.getItens().stream().map(pi -> {
            OrderItemDTO i = new OrderItemDTO();
            i.setId(pi.getId());
            i.setLivroId(pi.getLivro().getId());
            i.setTitulo(pi.getLivro().getTitulo());
            i.setQuantidade(pi.getQuantidade());
            i.setPrecoUnitario(pi.getPrecoUnitario());
            i.setSubtotal(pi.getSubtotal());
            return i;
        }).collect(Collectors.toList());
        dto.setItens(itens);

        // Calcular valorTotal somando subtotais dos itens
        BigDecimal valorTotal = itens.stream()
                .map(OrderItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setValorTotal(valorTotal);

        // Mapear endereço
        if (this.getEnderecoEntrega() != null) {
            EnderecoDTO enderecoDTO = new EnderecoDTO();
            enderecoDTO.setId(this.getEnderecoEntrega().getId());
            enderecoDTO.setTipoEndereco(this.getEnderecoEntrega().getTipoEndereco());
            enderecoDTO.setTipoLogradouro(this.getEnderecoEntrega().getTipoLogradouro());
            enderecoDTO.setNumero(this.getEnderecoEntrega().getNumero());
            enderecoDTO.setCidade(this.getEnderecoEntrega().getCidade());
            enderecoDTO.setCep(this.getEnderecoEntrega().getCep());
            dto.setEnderecoEntrega(enderecoDTO);
        }
        return dto;
    }


    private EnderecoDTO getEnderecoDTO(Pedido pedido) {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(pedido.getEnderecoEntrega().getId());
        enderecoDTO.setTipoEndereco(this.enderecoEntrega.getTipoEndereco());
        enderecoDTO.setTipoLogradouro(this.enderecoEntrega.getTipoLogradouro());
        enderecoDTO.setNumero(pedido.getEnderecoEntrega().getNumero());
        enderecoDTO.setCidade(pedido.getEnderecoEntrega().getCidade());
        enderecoDTO.setCep(pedido.getEnderecoEntrega().getCep());
        return enderecoDTO;
    }


}
