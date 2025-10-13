package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
import com.enzo.les.les.dtos.CarrinhoUpdateItemDTO;
import com.enzo.les.les.exceptions.BusinessException;
import com.enzo.les.les.exceptions.ResourceNotFoundException;
import com.enzo.les.les.model.entities.Carrinho;
import com.enzo.les.les.model.entities.CarrinhoItem;
import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.CarrinhoRepository;
import com.enzo.les.les.repository.ClienteRepository;
import com.enzo.les.les.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final CarrinhoRepository carrinhoRepository;

    @Autowired
    private final LivroRepository livroRepository;

    public CarrinhoService(ClienteRepository clienteRepository, CarrinhoRepository carrinhoRepository, LivroRepository livroRepository) {
        this.clienteRepository = clienteRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.livroRepository = livroRepository;
    }

    /**
     * Buscar o carrinho de um cliente pelo ID
     */
    @Transactional
    public Optional<CarrinhoDTO> buscarCarrinhoPorCliente(Long clienteId) {
        // Busca o carrinho existente
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId);

        if (carrinho == null) {
            // Cria apenas se realmente não existir
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));

            carrinho = new Carrinho();
            carrinho.setCliente(cliente);
            carrinho = carrinhoRepository.save(carrinho);
        }

        return Optional.of(carrinho.mapToDTO());
    }

    @Transactional
    public CarrinhoDTO adicionarItemPorCliente(Long clienteId, CarrinhoItemDTO dto) {
        // 1. Buscar carrinho do cliente
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId);

        // 2. Criar carrinho se não existir
        if (carrinho == null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));
            carrinho = new Carrinho();
            carrinho.setCliente(cliente);
            carrinho = carrinhoRepository.save(carrinho);
        }

        // 3. Adicionar o item
        return adicionarItem(carrinho.getId(), dto);
    }


    /**
     * Cria carrinho, se ainda não existir
     */
    @Transactional
    public CarrinhoDTO criarCarrinho(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));

        // Verifica se já existe
        Carrinho existente = carrinhoRepository.findByClienteId(clienteId);
        if (existente != null) {
            return existente.mapToDTO();
        }

        Carrinho novo = new Carrinho();
        novo.setCliente(cliente);
        novo = carrinhoRepository.save(novo);

        return novo.mapToDTO();
    }



    /**
     * Adiciona item ao carrinho
     */
    @Transactional
    public CarrinhoDTO adicionarItem(Long carrinhoId, CarrinhoItemDTO dto) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado: " + carrinhoId));

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + dto.getLivroId()));

        CarrinhoItem itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getLivro().getId().equals(dto.getLivroId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + dto.getQuantidade());
        } else {
            CarrinhoItem novoItem = new CarrinhoItem();
            novoItem.setCarrinho(carrinho);
            novoItem.setLivro(livro);
            novoItem.setQuantidade(dto.getQuantidade());
            carrinho.getItens().add(novoItem);
        }

        carrinho = carrinhoRepository.save(carrinho);
        return carrinho.mapToDTO();
    }

    /**
     * Remove um item do carrinho
     */
    public CarrinhoDTO removerItem(Long carrinhoId, Long livroId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));

        CarrinhoItem item = carrinho.getItens().stream()
                .filter(i -> i.getLivro().getId().equals(livroId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        carrinho.getItens().remove(item);
        carrinhoRepository.save(carrinho);

        return carrinho.mapToDTO();
    }

    /**
     * Atualiza a quantidade de um item
     */
    public CarrinhoDTO atualizarQuantidade(Long carrinhoId, CarrinhoUpdateItemDTO dto) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));

        CarrinhoItem item = carrinho.getItens().stream()
                .filter(i -> i.getLivro().getId().equals(dto.getLivroId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        if (dto.getQuantidade() <= 0) {
            throw new BusinessException("A quantidade mínima é 1 produto");
        }

        item.setQuantidade(dto.getQuantidade());
        carrinhoRepository.save(carrinho);

        return carrinho.mapToDTO();
    }
}
