package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.Docente;
import com.ems.system.entity.dto.DocenteDto;
import com.ems.system.entity.dto.QueryDto;

public interface DocenteService extends IService<Docente> {

    /** Lista paginada con busqueda difusa (blurry). */
    IPage<DocenteDto> queryDocenteTable(QueryDto queryDto);

    DocenteDto getDocente(Long id);

    /** Crea si el id es null, edita si trae id. */
    void editDocente(DocenteDto dto);

    void delDocente(Long id);
}