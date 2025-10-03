package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
import com.enzo.les.les.dtos.CarrinhoUpdateItemDTO;
import com.enzo.les.les.model.entities.Carrinho;
import com.enzo.les.les.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarrinhoService {
    @Autowired
    CarrinhoRepository carrinhoRepository;

    public Optional<CarrinhoDTO> buscarCarrinhoPorCliente(Long clienteId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId);
        return Optional.ofNullable(carrinho)
                .map(Carrinho::mapToDTO);
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
