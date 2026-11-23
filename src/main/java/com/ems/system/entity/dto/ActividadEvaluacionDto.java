package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ActividadEvaluacionDto {
    private Long id;
    private Long asignacionDocenteId;
    private Long periodoEvaluacionId;
    private String nombre;
    private BigDecimal ponderacion;
    private BigDecimal notaMaxima;
}
