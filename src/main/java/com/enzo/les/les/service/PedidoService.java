package com.enzo.les.les.service;

import com.enzo.les.les.dtos.*;
import com.enzo.les.les.exceptions.BusinessException;
import com.enzo.les.les.exceptions.InsufficientStockException;
import com.enzo.les.les.exceptions.ResourceNotFoundException;
import com.enzo.les.les.model.entities.*;
import com.enzo.les.les.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Autowired
    private SaldoEstoqueRepository saldoEstoqueRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CupomUsoRepository cupomUsoRepository;

    @Autowired
    private CartaoCreditoService cartaoCreditoService; // para salvar novo cartão

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Cria um pedido a partir do carrinho do cliente
     * - valida cupons
     * - reserva/baixa estoque usando lock pessimista
     * - cria pedido, itens (salvando precoUnitario), pagamentos e cupom_uso
     */
    @Transactional
    public CheckoutResponseDTO criarPedidoAPartirDoCarrinho(CheckoutRequestDTO dto) {
        // 1) buscar carrinho
        Carrinho carrinho = buscarCarrinhoValido(dto.getCarrinhoId());
        // 2) calcular valor total (somando preco atual * qtd)
        BigDecimal valorTotal = calcularValorTotal(carrinho);
        // 3) aplicar cupons (cupons de troca + cupom promocional) -> calcular desconto total
        BigDecimal descontoTotal = calcularDescontos(dto, valorTotal);
        // 4) somar pagamentos informados
        BigDecimal valorAPagar = valorTotal.subtract(descontoTotal);
        BigDecimal valorPago = validarPagamentos(dto, valorAPagar);
        // 5) determinar endereco de entrega (existente ou novo)
        Endereco enderecoEntrega = determinarEnderecoEntrega(dto);
        // 6) criar pedido (status ABERTO)
        Pedido pedido = criarPedido(carrinho, enderecoEntrega);
        // 7) salvar itens do pedido + reservar/baixar estoque com lock
        processarItensPedido(carrinho, pedido);
        processarPagamentos(dto, pedido);
        // 9) registrar uso de cupons (cupom_uso) — após pedido criado
        registrarUsoDeCupons(dto, pedido);
        // 10) limpar carrinho
        limparCarrinho(carrinho);
        // 11) resposta
        return montarCheckoutResponse(pedido, valorTotal, valorPago);
    }


    public List<OrderDTO> getPedidosByClienteId(long id){
        Cliente cliente =  clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return cliente.getPedidos()
                .stream()
                .map(Pedido::mapToDTO)
                .toList();
    }

    public List<OrderDTO> getAllPedidos(){
      return pedidoRepository.findAll().stream().map(Pedido::mapToDTO).toList();
    }
    /**
     * Consulta um pedido por ID - monta OrderDTO
     */
    public OrderDTO consultarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        OrderDTO dto = new OrderDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setClienteId(pedido.getCliente().getId());

        List<OrderItemDTO> itens = pedido.getItens().stream().map(pi -> {
            OrderItemDTO i = new OrderItemDTO();
            i.setLivroId(pi.getLivro().getId());
            i.setTitulo(pi.getLivro().getTitulo());
            i.setQuantidade(pi.getQuantidade());
            i.setPrecoUnitario(pi.getPrecoUnitario());
            i.setSubtotal(pi.getSubtotal());
            return i;
        }).collect(Collectors.toList());

        dto.setItens(itens);
        return dto;
    }

    private Carrinho buscarCarrinhoValido(Long carrinhoId){
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId).orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));
        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()){
            throw new BusinessException("Carrinho vazio");
        }
        return carrinho;
    }

    private BigDecimal calcularValorTotal(Carrinho carrinho){
        return carrinho.getItens().stream()
                .map(i -> BigDecimal.valueOf(i.getLivro().getPreco()).multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularDescontos(CheckoutRequestDTO dto, BigDecimal valorTotal) {
        BigDecimal descontoTotal = BigDecimal.ZERO;

        if (dto.getCupons() != null) {
            for (CupomUseDTO cu : dto.getCupons()) {
                Cupom cupom = cupomRepository.findById(cu.getCupomId())
                        .orElseThrow(() -> new BusinessException("Cupom não encontrado: " + cu.getCupomId()));
                if (!CupomService.validarCupom(cupom, valorTotal)) {
                    throw new BusinessException("Cupom inválido ou não aplicável: " + cupom.getCodigo());
                }
                descontoTotal = descontoTotal.add(calcularDescontoCupom(cupom, valorTotal));
            }
        }

        if (dto.getCupomPromocionalCodigo() != null && !dto.getCupomPromocionalCodigo().isBlank()) {
            Cupom promo = cupomRepository.findByCodigo(dto.getCupomPromocionalCodigo())
                    .orElseThrow(() -> new BusinessException("Cupom promocional inválido"));
            if (!CupomService.validarCupom(promo, valorTotal)) {
                throw new BusinessException("Cupom promocional inválido ou não aplicável");
            }
            descontoTotal = descontoTotal.add(calcularDescontoCupom(promo, valorTotal));
        }

        return descontoTotal;
    }

    private BigDecimal calcularDescontoCupom(Cupom cupom, BigDecimal valorTotal) {
        return cupom.isPercentual()
                ? valorTotal.multiply(
                BigDecimal.valueOf(cupom.getValor())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        )
                : BigDecimal.valueOf(cupom.getValor());
    }

    private BigDecimal validarPagamentos(CheckoutRequestDTO dto, BigDecimal valorAPagar){
        BigDecimal somaPagamentos = BigDecimal.ZERO;
        if(dto.getCartoesPagamento() != null){
            for(CartaoPagamentoDTO pc : dto.getCartoesPagamento()){
                if(pc.getValor() == null) throw new BusinessException("Valor pagamento não informado");
                somaPagamentos = somaPagamentos.add(pc.getValor());
            }
        }
        if (somaPagamentos.compareTo(valorAPagar) < 0){
            throw new BusinessException("Pagamento insuficiente. Valor a pagar: " + valorAPagar + " - Informado: " + somaPagamentos);
        }
        return somaPagamentos;
    }

    private Endereco determinarEnderecoEntrega(CheckoutRequestDTO dto){
        if(dto.getEnderecoEntregaId() != null){
            EnderecoDTO enderecoDTO = enderecoService.buscarPorId(dto.getEnderecoEntregaId());
            return enderecoDTO.mapToEntity();
        }else if(dto.getNovoEnderecoEntrega() != null){
            EnderecoDTO salvo = enderecoService.salvar(dto.getNovoEnderecoEntrega());
            return salvo.mapToEntity();
        }else{
            throw new BusinessException("Endereço de entrega não informado");
        }
    }

    private Pedido criarPedido(Carrinho carrinho, Endereco enderecoEntrega){
        Pedido pedido = new Pedido();
        pedido.setCliente(carrinho.getCliente());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setEnderecoEntrega(enderecoEntrega);
        return pedidoRepository.save(pedido);
    }

    private void processarItensPedido(Carrinho carrinho, Pedido pedido){
        for(CarrinhoItem ci : carrinho.getItens()){
            Long livroId = ci.getLivro().getId();
            SaldoEstoque saldo = saldoEstoqueRepository.findByLivroIdForUpdate(livroId).orElseThrow(()->new InsufficientStockException("Estoque não cadastrado para livro" + livroId));
            if(saldo.getQuantidade() < ci.getQuantidade()){
                throw new InsufficientStockException("Estoque insuficiente para livro" + livroId);
            }

            saldo.setQuantidade(saldo.getQuantidade() - ci.getQuantidade());
            saldoEstoqueRepository.save(saldo);
            PedidoItem pi = new PedidoItem();
            pi.setPedido(pedido);
            pi.setLivro(ci.getLivro());
            pi.setQuantidade(ci.getQuantidade());
            pi.setPrecoUnitario(BigDecimal.valueOf(ci.getLivro().getPreco()));
            pi.setSubtotal(pi.getPrecoUnitario().multiply(BigDecimal.valueOf(pi.getQuantidade())));
            pedidoItemRepository.save(pi);
        }
    }

    private void limparCarrinho(Carrinho carrinho){
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
    }

    private void processarPagamentos(CheckoutRequestDTO dto, Pedido pedido) {
        if (dto.getCartoesPagamento() == null || dto.getCartoesPagamento().isEmpty()) {
            return; // Pode haver casos em que o pagamento é 100% com cupons
        }

        for (CartaoPagamentoDTO pc : dto.getCartoesPagamento()) {
            Long cartaoId;

            if (pc.getCartaoId() != null) {
                // ✅ Cartão já cadastrado — usar diretamente
                cartaoId = pc.getCartaoId();
            } else if (pc.getNewCard() != null) {
                // 💳 Salvar novo cartão no cliente antes de usar no pagamento
                CartaoCreditoDTO novoCartaoDTO = pc.getNewCard();
                novoCartaoDTO.setClienteId(pedido.getCliente().getId());
                CartaoCreditoDTO salvo = cartaoCreditoService.salvar(novoCartaoDTO);
                cartaoId = salvo.getId();
            } else {
                throw new BusinessException("Cartão não informado em uma forma de pagamento");
            }

            CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cartão não encontrado: " + cartaoId));

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento("CARTAO");
            pagamento.setValor(pc.getValor());
            pagamento.setCartao(cartao);
            pagamento.setParcelas(pc.getParcelas() != null ? pc.getParcelas() : 1);

            pagamentoRepository.save(pagamento);
        }
    }

    private void registrarUsoDeCupons(CheckoutRequestDTO dto, Pedido pedido) {
        if ((dto.getCupons() == null || dto.getCupons().isEmpty())
                && (dto.getCupomPromocionalCodigo() == null || dto.getCupomPromocionalCodigo().isBlank())) {
            return;
        }

        Cliente cliente = pedido.getCliente();

        // 1) Cupons de troca (lista por id)
        if (dto.getCupons() != null) {
            for (CupomUseDTO cu : dto.getCupons()) {
                Cupom cupom = cupomRepository.findById(cu.getCupomId())
                        .orElseThrow(() -> new BusinessException("Cupom não encontrado: " + cu.getCupomId()));

                //impedir reaplicação pelo mesmo cliente (descomente se desejar)
                 if (cupomUsoRepository.existsByCupomIdAndClienteId(cupom.getId(), cliente.getId())) {
                     throw new BusinessException("Cupom já foi utilizado por este cliente: " + cupom.getCodigo());
                 }

                CupomUso uso = new CupomUso();
                uso.setCupom(cupom);
                uso.setPedido(pedido);
                uso.setCliente(cliente);
                cupomUsoRepository.save(uso);

                //se cupom for de uso único, desativar
                if (cupom.isSingleUse()) { cupom.setAtivo(false); cupomRepository.save(cupom); }
            }
        }

        // 2) Cupom promocional (código)
        if (dto.getCupomPromocionalCodigo() != null && !dto.getCupomPromocionalCodigo().isBlank()) {
            Cupom promo = cupomRepository.findByCodigo(dto.getCupomPromocionalCodigo())
                    .orElseThrow(() -> new BusinessException("Cupom promocional inválido"));

            // verificar se promo já foi usado pelo cliente
             if (cupomUsoRepository.existsByCupomIdAndClienteId(promo.getId(), cliente.getId())) {
                 throw new BusinessException("Cupom promocional já utilizado por este cliente: " + promo.getCodigo());
            }

            CupomUso usoProm = new CupomUso();
            usoProm.setCupom(promo);
            usoProm.setPedido(pedido);
            usoProm.setCliente(cliente);
            cupomUsoRepository.save(usoProm);

            // Opcional: desativar se for single-use
            if (promo.isSingleUse()) { promo.setAtivo(false); cupomRepository.save(promo); }
        }
    }

    private CheckoutResponseDTO montarCheckoutResponse(Pedido pedido, BigDecimal valorTotal, BigDecimal valorPago){
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setPedidoId(pedido.getId());
        response.setStatus(pedido.getStatus());
        response.setValorTotal(valorTotal);
        response.setValorPago(valorPago);
        return response;
    }



}
