package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String codigo;

    @OneToMany(mappedBy = "cupom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CupomUso> usos;

}
