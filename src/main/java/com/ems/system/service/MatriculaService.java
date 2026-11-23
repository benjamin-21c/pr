package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.Matricula;
import com.ems.system.entity.dto.MatriculaDto;
import com.ems.system.entity.dto.QueryDto;

import java.util.Map;

public interface MatriculaService extends IService<Matricula> {

    /** Lista paginada con busqueda difusa (blurry). */
    IPage<MatriculaDto> queryMatriculaTable(QueryDto queryDto);

    MatriculaDto getMatricula(Long id);

    /** Crea si el id es null, edita si trae id. */
    void editMatricula(MatriculaDto dto);

    void delMatricula(Long id);

    /** Opciones para los combos del formulario. */
    Map<String, Object> getMatriculaSelect();
}
