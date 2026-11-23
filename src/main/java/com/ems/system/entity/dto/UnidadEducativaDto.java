package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadEducativaDto {
    private Long id;
    private String codigo;
    private String nombre;
    private String municipio;
    private String distrito;
    private String direccion;
    private Boolean activo;
} 