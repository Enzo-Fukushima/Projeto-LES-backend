package com.enzo.les.les.model.dtos;


import com.enzo.les.les.enums.EstadoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;

    private String tipoResidencia;

    private String tipoLogradouro;

    @NotBlank(message = "Logradouro não pode ser vazio")
    private String logradouro;
    
    @NotNull(message = "Número não pode ser vazio")
    private Integer numero;
    
    @NotBlank(message = "Bairro não pode ser vazio")
    private String bairro;
    
    @NotBlank(message = "CEP não pode ser vazio")
    private String cep;
    
    @NotBlank(message = "Cidade não pode ser vazio")
    private String cidade;
    
    private EstadoEnum estado; 
    
    @NotBlank(message = "País não pode ser vazio")
    private String pais;
    
    @NotBlank(message = "Observações não pode ser vazio")
    private String observacoes;
    
    @NotBlank(message = "Cliente ID não pode ser vazio")
    private Long clienteId;
}
