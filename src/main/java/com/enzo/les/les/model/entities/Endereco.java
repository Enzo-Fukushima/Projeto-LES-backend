package com.enzo.les.les.model.entities;

import com.enzo.les.les.enums.EstadoEnum;
import com.enzo.les.les.enums.TipoEnderecoEnum;
import com.enzo.les.les.enums.TipoLogradouroEnum;
import com.enzo.les.les.enums.TipoResidenciaEnum;
import com.enzo.les.les.dtos.EnderecoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apelido;
    @Enumerated(EnumType.STRING)
    private TipoResidenciaEnum tipoResidencia;
    @Enumerated(EnumType.STRING)
    private TipoLogradouroEnum tipoLogradouro;
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cep;
    private String cidade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private EstadoEnum estado;
    private String pais;
    @Enumerated(EnumType.STRING)
    private TipoEnderecoEnum tipoEndereco;


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public EnderecoDTO mapToDTO() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(this.id);
        dto.setApelido(this.apelido);
        dto.setTipoResidencia(this.tipoResidencia);
        dto.setTipoLogradouro(this.tipoLogradouro);
        dto.setLogradouro(this.logradouro);
        dto.setNumero(this.numero);
        dto.setBairro(this.bairro);
        dto.setCep(this.cep);
        dto.setCidade(this.cidade);
        dto.setEstado(this.estado);
        dto.setPais(this.pais);
        if (this.cliente != null) {
            dto.setClienteId(this.cliente.getId());
        }
        return dto;
    }

    public void update(EnderecoDTO dto) {
        this.setApelido(dto.getApelido());
        this.setBairro(dto.getBairro());
        this.setCep(dto.getCep());
        this.setEstado(dto.getEstado());
        this.setLogradouro(dto.getLogradouro());
        this.setCidade(dto.getCidade());
        this.setTipoLogradouro(dto.getTipoLogradouro());
        this.setTipoResidencia(dto.getTipoResidencia());
        this.setNumero(dto.getNumero());
        this.setPais(dto.getPais());
    }

}
