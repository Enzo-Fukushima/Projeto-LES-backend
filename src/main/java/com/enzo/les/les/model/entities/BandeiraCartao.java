package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bandeiras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BandeiraCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; // Ex: VISA, MasterCard, Elo
}
