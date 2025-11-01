package com.enzo.les.les.service;

import com.enzo.les.les.dtos.*;
import com.enzo.les.les.enums.TipoCupomEnum;
import com.enzo.les.les.exceptions.BusinessException;
import com.enzo.les.les.exceptions.ResourceNotFoundException;
import com.enzo.les.les.model.entities.*;
import com.enzo.les.les.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrocaService {

    @Autowired
    private TrocaRepository trocaRepository;

    @Autowired
    private TrocaItemRepository trocaItemRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SaldoEstoqueRepository saldoEstoqueRepository;

    @Autowired
    private CupomRepository cupomRepository;

    /**
     * 1. SOLICITAR TROCA
     */
    @Transactional
    public TrocaDTO solicitarTroca(SolicitarTrocaDTO dto) {
        // Validações
        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Verificar se o pedido pertence ao cliente
        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("Este pedido não pertence ao cliente informado");
        }

        // Verificar se o pedido pode ser trocado (deve estar entregue)
        if (!"ENTREGUE".equalsIgnoreCase(pedido.getStatus())) {
            throw new BusinessException("Apenas pedidos entregues podem ser trocados");
        }

        // Verificar se já não existe uma troca para este pedido
        if (trocaRepository.existsByPedidoId(pedido.getId())) {
            throw new BusinessException("Já existe uma solicitação de troca para este pedido");
        }

        // Criar a troca
        Troca troca = new Troca();
        troca.setPedido(pedido);
        troca.setCliente(cliente);
        troca.setStatus("EM_TROCA");
        troca.setDataSolicitacao(LocalDateTime.now());
        troca.setMotivoTroca(dto.getMotivoTroca());

        BigDecimal valorTotal = BigDecimal.ZERO;

        // Adicionar itens da troca
        for (TrocaItemDTO itemDto : dto.getItens()) {
            PedidoItem pedidoItem = pedidoItemRepository.findById(itemDto.getPedidoItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item do pedido não encontrado"));

            // Verificar se o item pertence ao pedido
            if (!pedidoItem.getPedido().getId().equals(pedido.getId())) {
                throw new BusinessException("Item não pertence ao pedido informado");
            }

            // Verificar quantidade
            if (itemDto.getQuantidade() > pedidoItem.getQuantidade()) {
                throw new BusinessException("Quantidade de troca não pode ser maior que a quantidade do pedido");
            }

            TrocaItem trocaItem = new TrocaItem();
            trocaItem.setPedidoItem(pedidoItem);
            trocaItem.setQuantidade(itemDto.getQuantidade());
            trocaItem.setValorUnitario(pedidoItem.getPrecoUnitario());
            trocaItem.setMotivo(itemDto.getMotivo());
            trocaItem.calcularSubtotal();

            troca.addItem(trocaItem);
            valorTotal = valorTotal.add(trocaItem.getSubtotal());
        }

        troca.setValorTotalTroca(valorTotal);

        // Atualizar status do pedido
        pedido.setStatus("EM_TROCA");
        pedidoRepository.save(pedido);

        troca = trocaRepository.save(troca);

        return mapToDTO(troca);
    }

    /**
     * 2. AUTORIZAR TROCA (Admin)
     */
    @Transactional
    public TrocaDTO autorizarTroca(AutorizarTrocaDTO dto) {
        Troca troca = trocaRepository.findById(dto.getTrocaId())
                .orElseThrow(() -> new ResourceNotFoundException("Troca não encontrada"));

        if (!"EM_TROCA".equals(troca.getStatus())) {
            throw new BusinessException("Apenas trocas com status EM_TROCA podem ser autorizadas");
        }

        if (Boolean.TRUE.equals(dto.getAutorizada())) {
            troca.setStatus("TROCA_AUTORIZADA");
            troca.setDataAutorizacao(LocalDateTime.now());
            troca.setObservacaoAdmin(dto.getObservacao());
        } else {
            troca.setStatus("TROCA_NEGADA");
            troca.setDataAutorizacao(LocalDateTime.now());
            troca.setObservacaoAdmin(dto.getObservacao());

            // Restaurar status do pedido
            troca.getPedido().setStatus("ENTREGUE");
            pedidoRepository.save(troca.getPedido());
        }

        troca = trocaRepository.save(troca);

        return mapToDTO(troca);
    }

    /**
     * 3. VISUALIZAR TROCAS PENDENTES (Admin)
     */
    public List<TrocaDTO> listarTrocasPendentes() {
        List<Troca> trocas = trocaRepository.findTrocasPendentes();
        return trocas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 4. CONFIRMAR RECEBIMENTO E GERAR CUPOM (Admin)
     */
    @Transactional
    public TrocaDTO confirmarRecebimento(ConfirmarRecebimentoDTO dto) {
        Troca troca = trocaRepository.findById(dto.getTrocaId())
                .orElseThrow(() -> new ResourceNotFoundException("Troca não encontrada"));

        if (!"TROCA_AUTORIZADA".equals(troca.getStatus())) {
            throw new BusinessException("Apenas trocas autorizadas podem ter recebimento confirmado");
        }

        // Processar retorno ao estoque
        for (ItemRecebimentoDTO itemDto : dto.getItens()) {
            TrocaItem trocaItem = trocaItemRepository.findById(itemDto.getTrocaItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item da troca não encontrado"));

            if (!trocaItem.getTroca().getId().equals(troca.getId())) {
                throw new BusinessException("Item não pertence a esta troca");
            }

            trocaItem.setRetornarEstoque(itemDto.getRetornarEstoque());

            // Se deve retornar ao estoque, dar entrada
            if (Boolean.TRUE.equals(itemDto.getRetornarEstoque())) {
                Long livroId = trocaItem.getPedidoItem().getLivro().getId();
                SaldoEstoque saldo = saldoEstoqueRepository.findByLivroId(livroId)
                        .orElseThrow(() -> new BusinessException("Estoque não encontrado para o livro"));

                saldo.setQuantidade(saldo.getQuantidade() + trocaItem.getQuantidade());
                saldoEstoqueRepository.save(saldo);
            }

            trocaItemRepository.save(trocaItem);
        }

        troca.setStatus("TROCA_RECEBIDA");
        troca.setDataRecebimento(LocalDateTime.now());
        troca.setObservacaoAdmin(dto.getObservacao());

        // Gerar cupom de troca
        Cupom cupom = gerarCupomTroca(troca);
        troca.setCupomGerado(cupom);

        troca.setStatus("TROCA_CONCLUIDA");
        troca.setDataConclusao(LocalDateTime.now());

        troca = trocaRepository.save(troca);

        return mapToDTO(troca);
    }

    /**
     * GERAR CUPOM DE TROCA
     */
    private Cupom gerarCupomTroca(Troca troca) {
        Cupom cupom = new Cupom();
        cupom.setCodigo("TROCA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        cupom.setTipoCupom(TipoCupomEnum.TROCA);
        cupom.setValor(troca.getValorTotalTroca().doubleValue());
        cupom.setPercentual(false);
        cupom.setAtivo(true);
        cupom.setSingleUse(true);
        cupom.setValorMinimo(null);
        cupom.setDataValidade(LocalDate.now().plusMonths(6)); // Válido por 6 meses
        cupom.setCliente(troca.getCliente());
        cupom.setTroca(troca);

        return cupomRepository.save(cupom);
    }

    /**
     * BUSCAR TROCA POR ID
     */
    public TrocaDTO buscarPorId(Long id) {
        Troca troca = trocaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Troca não encontrada"));
        return mapToDTO(troca);
    }

    /**
     * LISTAR TROCAS DE UM CLIENTE
     */
    public List<TrocaDTO> listarTrocasCliente(Long clienteId) {
        List<Troca> trocas = trocaRepository.findByClienteId(clienteId);
        return trocas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * LISTAR TODAS AS TROCAS (Admin)
     */
    public List<TrocaDTO> listarTodasTrocas() {
        List<Troca> trocas = trocaRepository.findAll();
        return trocas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * LISTAR TROCAS POR STATUS
     */
    public List<TrocaDTO> listarTrocasPorStatus(String status) {
        List<Troca> trocas = trocaRepository.findByStatus(status);
        return trocas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * MAPPER: Entity -> DTO
     */
    private TrocaDTO mapToDTO(Troca troca) {
        List<TrocaItemDTO> itensDTO = troca.getItens().stream()
                .map(this::mapItemToDTO)
                .collect(Collectors.toList());

        return TrocaDTO.builder()
                .id(troca.getId())
                .pedidoId(troca.getPedido().getId())
                .clienteId(troca.getCliente().getId())
                .nomeCliente(troca.getCliente().getNome())
                .status(troca.getStatus())
                .dataSolicitacao(troca.getDataSolicitacao())
                .dataAutorizacao(troca.getDataAutorizacao())
                .dataRecebimento(troca.getDataRecebimento())
                .dataConclusao(troca.getDataConclusao())
                .motivoTroca(troca.getMotivoTroca())
                .observacaoAdmin(troca.getObservacaoAdmin())
                .valorTotalTroca(troca.getValorTotalTroca())
                .itens(itensDTO)
                .codigoCupom(troca.getCupomGerado() != null ? troca.getCupomGerado().getCodigo() : null)
                .build();
    }

    private TrocaItemDTO mapItemToDTO(TrocaItem item) {
        return TrocaItemDTO.builder()
                .id(item.getId())
                .pedidoItemId(item.getPedidoItem().getId())
                .livroId(item.getPedidoItem().getLivro().getId())
                .tituloLivro(item.getPedidoItem().getLivro().getTitulo())
                .quantidade(item.getQuantidade())
                .valorUnitario(item.getValorUnitario())
                .subtotal(item.getSubtotal())
                .retornarEstoque(item.getRetornarEstoque())
                .motivo(item.getMotivo())
                .build();
    }
}