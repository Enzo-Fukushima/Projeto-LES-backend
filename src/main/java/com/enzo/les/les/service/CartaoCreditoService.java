package com.enzo.les.les.service;



import com.enzo.les.les.model.dtos.CartaoCreditoDTO;
import com.enzo.les.les.model.entities.CartaoCredito;
import com.enzo.les.les.repository.CartaoCreditoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartaoCreditoService {
    @Autowired
    CartaoCreditoRepository cartaoCreditoRepository;


    //create
    public CartaoCreditoDTO salvar(CartaoCreditoDTO cartaoCreditoDTO) {
        CartaoCredito cartao = cartaoCreditoDTO.mapToEntity();
        cartaoCreditoRepository.save(cartao);

        return cartao.mapToDTO();
    }

    // READ - buscar por id
    public CartaoCreditoDTO buscarPorId(Long id) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        return cartao.mapToDTO();
    }

    // READ - listar todos
    public List<CartaoCreditoDTO> listarTodos() {
        return cartaoCreditoRepository.findAll()
                .stream()
                .map(CartaoCredito::mapToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public CartaoCreditoDTO atualizar(Long id, CartaoCreditoDTO dto) {
        CartaoCredito cartaoExistente = cartaoCreditoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cartao não encontrado com id " + id));

        cartaoExistente.update(dto);
        cartaoCreditoRepository.save(cartaoExistente);
        return cartaoExistente.mapToDTO();
    }

    // DELETE lógico (inativar)
    public void delete(Long id) {
        CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cartao não encontrado com id " + id));
        cartaoCreditoRepository.delete(cartaoCredito);
    }

}

