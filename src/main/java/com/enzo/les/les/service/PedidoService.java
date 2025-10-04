package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CheckoutRequestDTO;
import com.enzo.les.les.dtos.CheckoutResponseDTO;
import com.enzo.les.les.dtos.OrderDTO;
import com.enzo.les.les.dtos.OrderItemDTO;
import com.enzo.les.les.model.entities.*;
import com.enzo.les.les.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    CarrinhoRepository carrinhoRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    PedidoItemRepository pedidoItemRepository;
    @Autowired
    SaldoEstoqueRepository saldoEstoqueRepository;
    @Autowired
    EnderecoRepository enderecoClienteRepository;
    @Autowired
    PagamentoRepository pagamentoRepository;



    /**
     * Cria um pedido a partir do carrinho do cliente
     */
    @Transactional
    public CheckoutResponseDTO criarPedidoAPartirDoCarrinho(CheckoutRequestDTO dto) {
        // 1️⃣ Buscar carrinho
        Carrinho carrinho = carrinhoRepository.findById(dto.getCarrinhoId())
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        // 2️⃣ Validar estoque
        for (CarrinhoItem item : carrinho.getItens()) {
            SaldoEstoque saldo = saldoEstoqueRepository.findById(item.getLivro().getId())
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o livro: " + item.getLivro().getTitulo()));

            if (saldo.getQuantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o livro: " + item.getLivro().getTitulo());
            }
        }

        // 3️⃣ Criar Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(carrinho.getCliente());

        // Endereço de entrega
        Endereco endereco = enderecoClienteRepository.findById(dto.getEnderecoEntregaId())
                .orElseThrow(() -> new RuntimeException("Endereço de entrega não encontrado"));

        pedido.setEnderecoEntrega(endereco);

        // Status inicial
        pedido.setStatus("ABERTO");

        pedido = pedidoRepository.save(pedido);

        // 4️⃣ Criar Itens do Pedido
        for (CarrinhoItem item : carrinho.getItens()) {
            PedidoItem pi = new PedidoItem();
            pi.setPedido(pedido);
            pi.setLivro(item.getLivro());
            pi.setQuantidade(item.getQuantidade());
            pedidoItemRepository.save(pi);

            // Atualizar saldo de estoque
            SaldoEstoque saldo = saldoEstoqueRepository.findById(item.getLivro().getId())
                    .orElseThrow();
            saldo.setQuantidade(saldo.getQuantidade() - item.getQuantidade());
            saldoEstoqueRepository.save(saldo);
        }

        // 5️⃣ Pagamentos
        // (aqui você pode implementar lógica para múltiplos cartões, cupons etc.)
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamentoRepository.save(pagamento);

        // 6️⃣ Limpar carrinho
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);

        // 7️⃣ Montar resposta
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setPedidoId(pedido.getId());
        response.setStatus(pedido.getStatus());
        return response;
    }

    /**
     * Consulta um pedido por ID
     */
    public OrderDTO consultarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        OrderDTO dto = new OrderDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setClienteId(pedido.getCliente().getId());

        dto.setEnderecoEntrega(pedido.getEnderecoEntrega().mapToDTO());

        // Itens do pedido
        List<OrderItemDTO> itens = pedido.getItens().stream().map(pi -> {
            OrderItemDTO i = new OrderItemDTO();
            i.setLivroId(pi.getLivro().getId());
            i.setTitulo(pi.getLivro().getTitulo());
            i.setQuantidade(pi.getQuantidade());
            return i;
        }).collect(Collectors.toList());

        dto.setItens(itens);
        return dto;
    }

}
