package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BoletinEstudianteDto {
    private Integer gestion;
    private String materia;
    private Integer orden;
    private String periodo;
    private BigDecimal notaPeriodo;
    /** Suma de ponderaciones de las actividades del periodo (RB05: debe llegar a 100 para cerrar). */
    private BigDecimal ponderacionTotal;
    /** TRUE si la ponderacion suma 100 y todas las actividades tienen nota. */
    private Boolean completo;
}