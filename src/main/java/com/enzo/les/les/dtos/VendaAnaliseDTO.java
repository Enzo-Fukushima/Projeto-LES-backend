package com.enzo.les.les.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class VendaAnaliseDTO {
    private LocalDate data;
    private String categoria;
    private Long totalVendido;

    public VendaAnaliseDTO(LocalDate data, String categoria, Long totalVendido) {
        this.data = data;
        this.categoria = categoria;
        this.totalVendido = totalVendido;
    }
}
