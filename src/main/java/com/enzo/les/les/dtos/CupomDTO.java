package com.enzo.les.les.dtos;

import com.enzo.les.les.enums.TipoCupomEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupomDTO {
    private Long id;
    private String codigo;
    private TipoCupomEnum tipoCupom; //
    private Double valor;
    private boolean percentual;
    private boolean ativo;
    private boolean singleUse;
    private Double valorMinimo;
    private String dataValidade;
    private Long clienteId;
    private String nomeCliente;
    private Long trocaId;
}