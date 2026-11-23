package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EstudianteDto {
    private Long id;
    private Long personaId;
    private String codigoEstudiante;
    private LocalDate fechaIngreso;
    private Boolean activo;
}
