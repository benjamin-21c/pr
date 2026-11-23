package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.Estudiante;
import com.ems.system.entity.dto.EstudianteDto;
import com.ems.system.entity.dto.QueryDto;

public interface EstudianteService extends IService<Estudiante> {

    /** Lista paginada con busqueda difusa (blurry). */
    IPage<EstudianteDto> queryEstudianteTable(QueryDto queryDto);

    EstudianteDto getEstudiante(Long id);

    /** Crea si el id es null, edita si trae id. */
    void editEstudiante(EstudianteDto dto);

    void delEstudiante(Long id);
}

