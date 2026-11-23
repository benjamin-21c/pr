package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteDto {
    private Long id;
    private Long personaId;
    private String registroDocente;
    private String especialidad;
    private Boolean activo;
}