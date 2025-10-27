package com.enzo.les.les.dtos;

import com.enzo.les.les.enums.TipoCupomEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupomUseDTO {
    private Long id;
    private Long cupomId;
    private String codigo;
    private TipoCupomEnum tipo;
    private Double valor;
    private Boolean percentual;
    private Double valorMinimo;
    private Boolean ativo;
    private Boolean singleUse;
    private String dataValidade;
}