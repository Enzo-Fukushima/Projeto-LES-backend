package com.enzo.les.les.dtos;

import lombok.Data;

@Data
public class AnalyticsRequestDTO {
    private String tipo; // "PRODUTO" ou "CATEGORIA"
    private Long id;
    private String dataInicio; // formato: YYYY-MM-DD
    private String dataFim; // formato: YYYY-MM-DD
}