package com.enzo.les.les.model.entities;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "livros") // ✅ IMPORTANTE: Exclui livros do toString
@EqualsAndHashCode(exclude = "livros") // ✅ IMPORTANTE: Exclui livros do equals/hashCode
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private Set<Livro> livros;
}