package com.enzo.les.les.dtos;


import com.enzo.les.les.enums.EstadoEnum;
import com.enzo.les.les.enums.TipoEnderecoEnum;
import com.enzo.les.les.model.entities.Endereco;
import com.enzo.les.les.enums.TipoLogradouroEnum;
import com.enzo.les.les.enums.TipoResidenciaEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;

    @NotNull(message = "Tipo de residência não pode ser vazio")
    private TipoResidenciaEnum tipoResidencia;

    @NotNull(message = "Tipo de logradouro não pode ser vazio")
    private TipoLogradouroEnum tipoLogradouro;

    @NotNull(message = "Tipo de endereço não pode ser vazio")
    private TipoEnderecoEnum tipoEndereco;

    @NotBlank(message = "Apelido não pode ser vazio")
    private String apelido;

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
    
    @NotNull(message = "Estado não pode ser vazio")
    private EstadoEnum estado; 
    
    @NotBlank(message = "País não pode ser vazio")
    private String pais;
    
    @NotNull(message = "Cliente ID não pode ser vazio")
    private Long clienteId;


    public Endereco mapToEntity() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setApelido(this.apelido);
        endereco.setTipoResidencia(this.tipoResidencia);
        endereco.setTipoLogradouro(this.tipoLogradouro);
        endereco.setTipoEndereco(this.tipoEndereco);
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setBairro(this.bairro);
        endereco.setCep(this.cep);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setPais(this.pais);

        return endereco;
    } 
}
