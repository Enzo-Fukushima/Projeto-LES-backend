package com.enzo.les.les.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Data                   // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: gera construtor sem argumentos
@AllArgsConstructor     // Lombok: gera construtor com todos os argumentos
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String telefone;

    private String senha;

    private boolean ativo = true; // por padrão, cliente começa ativo
}
