package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "saldo_estoque")
@Getter
@Setter
public class SaldoEstoque {

    @Id
    @Column(name = "livro_id")
    private Long livroId;

    @OneToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(nullable = false)
    private Integer quantidade = 0;
}
