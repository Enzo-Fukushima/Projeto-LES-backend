package com.enzo.les.les.dtos;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.model.entities.Categoria; // <<< IMPORT NECESSÁRIO AQUI

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String descricao;
    private Long editoraId;
    private Double preco;
    private String publicacao;
    private Integer estoque;
    private Double peso;
    private String imagem_url; // Mantido por compatibilidade com sua Entity

    // IDs e nomes de categorias
    private Set<Long> categoriaIds;
    private Set<String> categoriaNomes;
    
    // Construtor obrigatório para a referência de método 'LivroDTO::new' no Service,
    // que mapeia a Entity para o DTO.
    public LivroDTO(Livro entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.autor = entity.getAutor();
        this.descricao = entity.getDescricao();
        this.editoraId = entity.getEditora() != null ? entity.getEditora().getId() : null;
        this.preco = entity.getPreco();
        this.publicacao = entity.getPublicacao();
        this.estoque = entity.getEstoque();
        this.peso = entity.getPeso();
        this.imagem_url = entity.getImagemUrl();

        // Mapeia categorias: O Livro.java usa um Set<Categoria>, que está mapeado corretamente.
        if (entity.getCategorias() != null) {
            this.categoriaIds = entity.getCategorias().stream()
                .map(Categoria::getId) 
                .collect(Collectors.toSet());
            this.categoriaNomes = entity.getCategorias().stream()
                .map(Categoria::getNome)
                .collect(Collectors.toSet());
        } else {
            this.categoriaIds = Collections.emptySet();
            this.categoriaNomes = Collections.emptySet();
        }
    }
}