package com.enzo.les.les.model.entities;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.enzo.les.les.dtos.LivroDTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"categorias", "saldoEstoque", "editora"}) // ✅ Exclui relacionamentos
@EqualsAndHashCode(exclude = {"categorias", "saldoEstoque", "editora"}) // ✅ Exclui relacionamentos
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(nullable = false, length = 150)
    private String autor;

    @Column(length = 1000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editora_id")
    private Editora editora;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private String publicacao;

    @OneToOne(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SaldoEstoque saldoEstoque;

    @Column
    private Double peso;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    public Integer getEstoque() {
        return saldoEstoque != null ? saldoEstoque.getQuantidade() : 0;
    }

    public void setEstoque(Integer quantidade) {
        if (saldoEstoque == null) {
            saldoEstoque = new SaldoEstoque();
            saldoEstoque.setLivro(this);
        }
        saldoEstoque.setQuantidade(quantidade != null ? quantidade : 0);
    }

    public LivroDTO mapToDTO() {
        Set<Long> categoriaIds = (categorias == null ? Collections.emptySet() :
                categorias.stream().map(Categoria::getId).collect(Collectors.toSet()));

        Set<String> categoriaNomes = (categorias == null ? Collections.emptySet() :
                categorias.stream().map(Categoria::getNome).collect(Collectors.toSet()));

        return LivroDTO.builder()
                .id(this.id)
                .titulo(this.titulo)
                .autor(this.autor)
                .editoraId(this.editora != null ? this.editora.getId() : null)
                .preco(this.preco)
                .estoque(this.getEstoque())
                .categoriaIds(categoriaIds)
                .categoriaNomes(categoriaNomes)
                .build();
    }
}