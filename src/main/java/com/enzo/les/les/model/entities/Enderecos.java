package com.enzo.les.les.model.entities;

import jakarta.persistence.*;

public class Enderecos {
    @Id
    @GeneratedValue
    private long id;

    private String logradouro;
    private Integer numero;
    private String bairro;
    private Integer cep;
    private String cidade;

}
