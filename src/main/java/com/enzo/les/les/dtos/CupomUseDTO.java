package com.enzo.les.les.dtos;

import com.enzo.les.les.enums.TipoCupomEnum;
import com.enzo.les.les.model.entities.Cupom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupomUseDTO {
    // Pode enviar id ou codigo (se quiser validar por código)
    private Long cupomId;
    private String codigo;
    // valor aplicado (opcional - servidor pode calcular)
    private TipoCupomEnum tipoCupom;

    private Double valor;

    public CupomUseDTO(Cupom cupom) {
        CupomUseDTO dto = new CupomUseDTO();
        dto.setCupomId(cupom.getId());
        dto.setTipoCupom(cupom.getTipoCupom());
        dto.setCodigo(cupom.getCodigo());
    }
}