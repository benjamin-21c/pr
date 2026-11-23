package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class PersonaDto {
    private Long id;
    private Long userId;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String documento;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String direccion;
}