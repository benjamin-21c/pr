package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoDto {
    private Long id;
    private Long unidadEducativaId;
    private Long gestionId;
    private Long gradoId;
    private Long paraleloId;
    private String turno;
    private Integer cupo;
}