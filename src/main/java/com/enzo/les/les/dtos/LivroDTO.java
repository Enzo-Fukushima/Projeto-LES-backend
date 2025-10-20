package com.enzo.les.les.dtos;

import java.util.Set;

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
    private String imagem_url;

    // Agora apenas IDs e nomes de categorias
    private Set<Long> categoriaIds;
    private Set<String> categoriaNomes;

    private String imagemUrl;
}
