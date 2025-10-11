package com.enzo.les.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroDTO {

    private Long id;
    private String codigo;
    private String titulo;
    private Double preco;
    private String autor;

    private Long grupoPrecificacaoId;
    private String grupoPrecificacaoNome;

    private Long editoraId;
    private String editoraNome;

    private Set<Long> categoriaIds;
    private Set<String> categoriaNomes;
}
