package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CheckoutRequestDTO;
import com.enzo.les.les.dtos.CheckoutResponseDTO;
import com.enzo.les.les.dtos.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    public CheckoutResponseDTO criarPedidoAPartirDoCarrinho(CheckoutRequestDTO dto) {
        // TODO: Validar carrinho, calcular valores, gerar pedido, pagamentos e cupons
        return new CheckoutResponseDTO();
    }

    public OrderDTO consultarPedido(Long id) {
        // TODO: Buscar pedido por ID no banco
        return new OrderDTO();
    }
}
