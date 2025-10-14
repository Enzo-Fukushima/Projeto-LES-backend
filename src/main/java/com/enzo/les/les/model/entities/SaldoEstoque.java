package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saldo_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "livro") // ✅ IMPORTANTE: Exclui livro do toString
@EqualsAndHashCode(exclude = "livro") // ✅ IMPORTANTE: Exclui livro do equals/hashCode
public class SaldoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "livro_id", nullable = false, unique = true)
    private Livro livro;

    @Column(nullable = false)
    private Integer quantidade = 0;
}
