package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NotaDocenteDto {
    private String codigoEstudiante;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String periodo;
    private String actividad;
    private BigDecimal ponderacion;
    private BigDecimal notaMaxima;
    private BigDecimal nota;
    private String observacion;
}