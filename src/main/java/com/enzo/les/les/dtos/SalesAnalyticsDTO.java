package com.enzo.les.les.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesAnalyticsDTO {
    private LocalDate data;
    private Long quantidade;
    private BigDecimal valorTotal;
}