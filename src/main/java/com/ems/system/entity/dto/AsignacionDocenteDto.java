package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionDocenteDto {
    private Long id;
    private Long cursoId;
    private Long planMateriaId;
    private Long docenteId;
    private Boolean activo;
}