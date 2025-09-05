package com.enzo.les.les.model.dtos;


import com.enzo.les.les.enums.EstadoEnum;
import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.model.entities.Endereco;
import com.enzo.les.les.enums.TipoLogradouroEnum;
import com.enzo.les.les.enums.TipoResidenciaEnum;

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

    private Cliente Cliente;

    // 🔹 Conversão para entidade Endereco
    public Endereco mapToEntity() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setTipoResidencia(TipoResidenciaEnum.valueOf(this.tipoResidencia));
        endereco.setTipoLogradouro(TipoLogradouroEnum.valueOf(this.tipoLogradouro));
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setBairro(this.bairro);
        endereco.setCep(this.cep);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setPais(this.pais);
        endereco.setObservacoes(this.observacoes);
        // Cliente deve ser setado no serviço
        return endereco;
    } 
}
