package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.Curso;
import com.ems.system.entity.dto.CursoDto;
import com.ems.system.entity.dto.QueryDto;

import java.util.Map;

public interface CursoService extends IService<Curso> {

    /** Lista paginada con busqueda difusa (blurry). */
    IPage<CursoDto> queryCursoTable(QueryDto queryDto);

    CursoDto getCurso(Long id);

    /** Crea si el id es null, edita si trae id. */
    void editCurso(CursoDto dto);

    void delCurso(Long id);

    /** Opciones para los combos del formulario. */
    Map<String, Object> getCursoSelect();
}
