package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MateriaDto {
    private Long id;
    private String codigo;
    private String nombre;
    private Boolean activo;
}
