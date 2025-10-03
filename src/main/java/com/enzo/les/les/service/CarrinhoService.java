package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoUpdateItemDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {
    public CarrinhoDTO buscarCarrinhoPorCliente(Long clienteId) {

        return new CarrinhoDTO();
    }

    public CarrinhoDTO adicionarItem(Long carrinhoId, CarrinhoItemDTO dto) {
        // TODO: Adicionar item ao carrinho no banco
        return new CarrinhoDTO();
    }

    public CarrinhoDTO atualizarItem(Long carrinhoId, CarrinhoUpdateItemDTO dto) {
        // TODO: Atualizar quantidade ou remover item
        return new CarrinhoDTO();
    }
}
