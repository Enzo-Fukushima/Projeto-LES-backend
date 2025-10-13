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
import jakarta.persistence.Table;
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

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(nullable = false, length = 150)
    private String autor;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, length = 150)
    private String editora;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private String publicacao; // formato "YYYY-MM-DD"

    @Column(nullable = false)
    private Integer estoque;

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

    public LivroDTO mapToDTO() {
        // Evita NullPointerException caso categorias seja nulo
        Set<Long> categoriaIds = (categorias == null ? Collections.emptySet() :
                categorias.stream().map(Categoria::getId).collect(Collectors.toSet()));

        Set<String> categoriaNomes = (categorias == null ? Collections.emptySet() :
                categorias.stream().map(Categoria::getNome).collect(Collectors.toSet()));

        return LivroDTO.builder()
                .id(this.id)
                .titulo(this.titulo)
                .autor(this.autor)
                .editora(this.editora)
                .preco(this.preco)
                .estoque(this.estoque != null ? this.estoque : 0)
                .categoriaIds(categoriaIds)
                .categoriaNomes(categoriaNomes)
                .build();
    }
}
