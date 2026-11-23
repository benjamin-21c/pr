package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatriculaCursoDto {
    private String unidadEducativa;
    private Integer gestion;
    private String grado;
    private Integer numeroGrado;
    private String paralelo;
    private String turno;
    private Integer cupo;
    private Long matriculadosActivos;
    private Long matriculasTotal;
}