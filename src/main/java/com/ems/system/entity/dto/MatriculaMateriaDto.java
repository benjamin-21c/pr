package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 

public class MatriculaMateriaDto {
    private Long id;
    private Long matriculaId;
    private Long asignacionDocenteId;
    private String estado;
}
