package com.enzo.les.les.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CupomUseDTO {
    // Pode enviar id ou codigo (se quiser validar por código)
    private Long cupomId;
    private String codigo;
    // valor aplicado (opcional - servidor pode calcular)
}
