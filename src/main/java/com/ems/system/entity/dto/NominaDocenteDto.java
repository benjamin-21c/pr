package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NominaDocenteDto {
    private Long matriculaMateriaId;
    private String codigoEstudiante;
    private String documento;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String estado;
}