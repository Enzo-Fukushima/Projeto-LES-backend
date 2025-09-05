package com.enzo.les.les.model.entities;

import com.enzo.les.les.enums.TipoLogradouro;
import com.enzo.les.les.enums.TipoResidencia;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoResidencia tipoResidencia;
    @Enumerated(EnumType.STRING)
    private TipoLogradouro tipoLogradouro;
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
