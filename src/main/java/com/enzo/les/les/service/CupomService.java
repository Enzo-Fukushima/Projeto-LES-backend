package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CarrinhoDTO;
import com.enzo.les.les.dtos.CupomUseDTO;
import com.enzo.les.les.exceptions.BusinessException;
import com.enzo.les.les.exceptions.ResourceNotFoundException;
import com.enzo.les.les.model.entities.Carrinho;
import com.enzo.les.les.model.entities.Cupom;
import com.enzo.les.les.repository.CarrinhoRepository;
import com.enzo.les.les.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    // Criar cupom
    public Cupom criarCupom(Cupom cupom) {
        return cupomRepository.save(cupom);
    }

    // Buscar cupom por código
    public Optional<Cupom> buscarPorCodigo(String codigo) {
        return cupomRepository.findByCodigo(codigo);
    }

    // Validar cupom
    public static boolean validarCupom(Cupom cupom, BigDecimal valorCompra) {
        if (!cupom.isAtivo()) return false;
        if (cupom.getDataValidade()!= null && cupom.getDataValidade().isBefore(LocalDate.now())) return false;
        if (cupom.getValorMinimo() != null && valorCompra.compareTo(BigDecimal.valueOf(cupom.getValorMinimo())) < 0) return false;
        return true;
    }

    // Aplicar cupom em um carrinho
    public CarrinhoDTO aplicarCupom(Long carrinhoId, String codigoCupom) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        Cupom cupom = cupomRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> new RuntimeException("Cupom inválido"));

        BigDecimal total = BigDecimal.valueOf(carrinho.getItens().stream()
                .mapToDouble(i -> i.getLivro().getPreco() * i.getQuantidade())
                .sum());

        if (!validarCupom(cupom, total)) {
            throw new IllegalArgumentException("Cupom inválido ou não aplicável");
        }

        BigDecimal desconto;
        if (cupom.isPercentual()) {
            desconto = total.multiply(BigDecimal.valueOf(cupom.getValor() / 100.0));
        } else {
            desconto = BigDecimal.valueOf(cupom.getValor());
        }

        carrinho.setDesconto(desconto); // precisa ter esse campo em Carrinho
        carrinho.setCupom(cupom);       // relacionamento opcional

        carrinhoRepository.save(carrinho);
        return carrinho.mapToDTO();
    }

    // Desativar cupom manualmente
    public void desativarCupom(Long id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
        cupom.setAtivo(false);
        cupomRepository.save(cupom);
    }

    public CupomUseDTO validarCupom(String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));

        boolean valido = validarCupom(cupom, BigDecimal.ZERO); // ou passe o valor da compra, se aplicável
        if (!valido) throw new BusinessException("Cupom inválido ou expirado");

        return new CupomUseDTO(cupom);
    }
}

