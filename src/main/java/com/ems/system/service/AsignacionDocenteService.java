package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.AsignacionDocente;
import com.ems.system.entity.dto.AsignacionDocenteDto;
import com.ems.system.entity.dto.QueryDto;

import java.util.Map;

public interface AsignacionDocenteService extends IService<AsignacionDocente> {

    /** Lista paginada con busqueda difusa (blurry). */
    IPage<AsignacionDocenteDto> queryAsignacionDocenteTable(QueryDto queryDto);

    AsignacionDocenteDto getAsignacionDocente(Long id);

    /** Crea si el id es null, edita si trae id. */
    void editAsignacionDocente(AsignacionDocenteDto dto);

    void delAsignacionDocente(Long id);

    /** Opciones para los combos del formulario. */
    Map<String, Object> getAsignacionDocenteSelect();
}
