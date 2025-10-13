package com.enzo.les.les.model.entities;

import java.util.Set;

import com.enzo.les.les.dtos.LivroDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 300)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "grupo_precificacao_id")
    private GrupoPrecificacao grupoPrecificacao;

    private Double preco;

    @Column(nullable = false, length = 50)
    private String autor;

    @ManyToOne
    @JoinColumn(name = "editora_id")
    private Editora editora;

    @ManyToMany
    @JoinTable(
        name = "livro_categoria",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias;

    @OneToOne(mappedBy = "livro")
    private SaldoEstoque saldoEstoque;


    public LivroDTO mapToDTO(){
        return LivroDTO.builder()
                .id(this.id)
                .codigo(this.codigo)
                .titulo(this.titulo)
                .preco(this.preco)
                .autor(this.autor)
                .estoque(this.saldoEstoque != null ? this.saldoEstoque.getQuantidade() : 0) // <- estoque
                .editoraId(this.editora != null ? this.editora.getId() : null)
                .editoraNome(this.editora != null ? this.editora.getNome() : null)
                .categoriaIds(this.categorias != null
                        ? this.categorias.stream().map(Categoria::getId).collect(java.util.stream.Collectors.toSet())
                        : java.util.Collections.emptySet())
                .categoriaNomes(this.categorias != null
                        ? this.categorias.stream().map(Categoria::getNome).collect(java.util.stream.Collectors.toSet())
                        : java.util.Collections.emptySet())
                .build();
    }
}
