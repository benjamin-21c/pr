package com.ems.system.entity.dto;
 
import lombok.Getter;
import lombok.Setter;
 
import java.util.Date;
import java.util.List;
 
@Setter
@Getter
public class CasasDto {
 
    private Long idCasa;
 
    private String nombreCasa;
 
    private String direccion;
 
    private String ciudad;
 
    private Long cantidadPisos;
    private Date fechaConstruccion;
    private List<CuartosDto> cuartos;
}

