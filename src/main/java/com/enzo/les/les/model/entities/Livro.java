package com.enzo.les.les.model.entities;

import java.util.Set;

import com.enzo.les.les.dtos.LivroDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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


    public LivroDTO mapToDTO(){
        return LivroDTO.builder()
                .id(this.id)
                .codigo(this.codigo)
                .titulo(this.titulo)
                .preco(this.preco)
                .autor(this.autor)
                .grupoPrecificacaoId(this.grupoPrecificacao != null ? this.grupoPrecificacao.getId() : null)
                .grupoPrecificacaoNome(this.grupoPrecificacao != null ? this.grupoPrecificacao.getNome() : null)
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
