package com.enzo.les.les.model.entities;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.enzo.les.les.dtos.LivroDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
                .descricao(this.descricao)
                .imagem_url(this.imagemUrl)
                .publicacao(this.publicacao)
                .estoque(this.getEstoque())
                .categoriaIds(categoriaIds)
                .categoriaNomes(categoriaNomes)
                .build();
    }
}