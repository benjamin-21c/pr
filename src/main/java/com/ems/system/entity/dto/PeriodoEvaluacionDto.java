package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class PeriodoEvaluacionDto {
    private Long id;
    private Long gestionId;
    private String nombre;
    private Integer orden;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
