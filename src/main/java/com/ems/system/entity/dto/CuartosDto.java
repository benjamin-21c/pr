package com.ems.system.entity.dto;
 
import lombok.Getter;
import lombok.Setter;
 
import java.math.BigDecimal;
 
@Setter
@Getter
public class CuartosDto {
 
    private Long idCuarto;
 
    private Long idCasa;
 
    private String nombreCuarto;
 
    private String tipoCuarto;
 
    private BigDecimal areaM2;
 
    private Boolean tieneVentana;
}

