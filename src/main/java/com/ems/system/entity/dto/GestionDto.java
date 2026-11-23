package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class GestionDto {
    private Long id;
    private Integer anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}
