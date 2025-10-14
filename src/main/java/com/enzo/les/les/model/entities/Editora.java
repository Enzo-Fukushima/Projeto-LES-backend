package com.enzo.les.les.model.entities;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "editoras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "livros") // ✅ IMPORTANTE: Exclui livros do toString
@EqualsAndHashCode(exclude = "livros") // ✅ IMPORTANTE: Exclui livros do equals/hashCode
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @OneToMany(mappedBy = "editora", fetch = FetchType.LAZY)
    private Set<Livro> livros;
}