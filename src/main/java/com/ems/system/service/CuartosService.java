package com.ems.system.service;
 
import com.ems.system.entity.Cuartos;
 
import java.util.List;
 
public interface CuartosService {
 
    List<Cuartos> findByCasaId(Long idCasa);
}
