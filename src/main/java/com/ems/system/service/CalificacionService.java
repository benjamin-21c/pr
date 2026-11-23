package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ems.system.entity.Calificacion;
import com.ems.system.entity.dto.CalificacionDto;
import com.ems.system.entity.dto.QueryDto;

public interface CalificacionService {

    IPage<Calificacion> queryCalificacionTable(QueryDto queryDto);

    CalificacionDto getCalificacion(Long id);

    void editCalificacion(CalificacionDto dto);

    void delCalificacion(Long id);
}