package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovoCardDTO {
    // Não guarde CVV em banco em produção sem tokenização — este DTO aceita CVV para processar no momento
    @NotBlank
    @Size(min = 12, max = 19)
    private String numero;

    @NotBlank
    private String nomeTitular;

    @NotBlank
    @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2}|\\d{4})$", message = "Formato MM/YY ou MM/YYYY")
    private String validade;

    @NotBlank
    @Size(min = 3, max = 4)
    private String codigoSeguranca;

    private String bandeira; // opcional: VISA, MASTERCARD, AMEX, ELO, etc.

}
