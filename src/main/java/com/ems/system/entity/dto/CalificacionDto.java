package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalificacionDto {
    private Long id;
    private Long matriculaMateriaId;
    private Long actividadEvaluacionId;
    private BigDecimal nota;
    private String observacion;
    private Long registradoPor;
    private LocalDateTime fechaRegistro;
}