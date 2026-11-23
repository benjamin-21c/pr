package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.UnidadEducativa;
import com.ems.system.entity.dto.QueryDto;

public interface UnidadEducativaService extends IService<UnidadEducativa> {

    /** Lista paginada con busqueda difusa (blurry) por nombre o codigo. */
    IPage<UnidadEducativa> queryUnidadEducativaTable(QueryDto queryDto);

    UnidadEducativa getUnidadEducativa(Long id);

    /** Crea si id es null, edita si trae id. */
    void editUnidadEducativa(UnidadEducativa unidadEducativa);

    void delUnidadEducativa(Long id);
}