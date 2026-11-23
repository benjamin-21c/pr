package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class MatriculaDto {
    private Long id;
    private Long estudianteId;
    private Long cursoId;
    private Long gestionId;
    private LocalDate fechaMatricula;
    private String estado;
}