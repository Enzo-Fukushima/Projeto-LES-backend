package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trocas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Troca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 50)
    private String status; // EM_TROCA, TROCA_AUTORIZADA, TROCA_RECEBIDA, TROCA_CONCLUIDA, TROCA_NEGADA

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_autorizacao")
    private LocalDateTime dataAutorizacao;

    @Column(name = "data_recebimento")
    private LocalDateTime dataRecebimento;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String motivoTroca;

    @Column(columnDefinition = "TEXT")
    private String observacaoAdmin;

    @Column(name = "valor_total_troca", precision = 10, scale = 2)
    private BigDecimal valorTotalTroca;

    @OneToMany(mappedBy = "troca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrocaItem> itens = new ArrayList<>();

    @OneToOne(mappedBy = "troca", cascade = CascadeType.ALL)
    private Cupom cupomGerado;

    // metodo auxiliar para adicionar item
    public void addItem(TrocaItem item) {
        itens.add(item);
        item.setTroca(this);
    }

    // Metodo auxiliar para remover item
    public void removeItem(TrocaItem item) {
        itens.remove(item);
        item.setTroca(null);
    }
}