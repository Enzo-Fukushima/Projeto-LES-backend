package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CarrinhoItemDTO;
import com.enzo.les.les.dtos.CarrinhoUpdateItemDTO;
import com.enzo.les.les.model.entities.Carrinho;
import com.enzo.les.les.model.entities.CarrinhoItem;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.CarrinhoRepository;
import com.enzo.les.les.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarrinhoService {
    @Autowired
    CarrinhoRepository carrinhoRepository;

    @Autowired
    LivroRepository livroRepository;

    public Optional<CarrinhoDTO> buscarCarrinhoPorCliente(Long clienteId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId);
        return Optional.ofNullable(carrinho)
                .map(Carrinho::mapToDTO);
    }

    public CarrinhoDTO adicionarItem(Long carrinhoId, CarrinhoItemDTO dto) {
        // 1. Buscar o carrinho
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        // 2. Buscar o produto
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 3. Verificar se o item já existe no carrinho
        Optional<CarrinhoItem> itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getLivro().getId().equals(livro.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Se já existe, só aumenta a quantidade
            CarrinhoItem item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + dto.getQuantidade());
        } else {
            // Se não existe, cria um novo item
            CarrinhoItem novoItem = new CarrinhoItem();
            novoItem.setLivro(livro);
            novoItem.setQuantidade(dto.getQuantidade());
            novoItem.setCarrinho(carrinho);

            carrinho.getItens().add(novoItem);
        }

        // 4. Salvar o carrinho atualizado
        carrinhoRepository.save(carrinho);

        // 5. Retornar DTO
        return carrinho.mapToDTO();
    }

    public CarrinhoDTO removerItem(Long carrinhoId, Long livroId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        CarrinhoItem item = carrinho.getItens().stream()
                .filter(i -> i.getLivro().getId().equals(livroId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho"));

        carrinho.getItens().remove(item);
        carrinhoRepository.save(carrinho);
        return carrinho.mapToDTO();
    }

    public CarrinhoDTO atualizarQuantidade(Long carrinhoId, CarrinhoUpdateItemDTO dto){
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        CarrinhoItem item = carrinho.getItens().stream().filter(i -> i.getLivro().getId().equals(dto.getLivroId())).findFirst().orElseThrow(()-> new RuntimeException("Item não encontrado"));
        if(dto.getQuantidade() <= 0) throw new IllegalArgumentException("A quantidade mínima é 1 produto");

        item.setQuantidade(dto.getQuantidade());
        carrinhoRepository.save(carrinho);
        return carrinho.mapToDTO();
    }
}
