package com.enzo.les.les.model.entities;
import com.enzo.les.les.enums.TipoCupomEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "valor_minimo")
    private Double valorMinimo;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private boolean percentual; // true = %, false = valor fixo

    private TipoCupomEnum tipoCupom;

    @OneToMany(mappedBy = "cupom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CupomUso> usos;

    private boolean singleUse;

}
