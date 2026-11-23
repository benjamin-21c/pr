package com.ems.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.Materia;
import com.ems.system.entity.dto.QueryDto;

public interface MateriaService extends IService<Materia> {

    /** Lista paginada con busqueda difusa (blurry) por nombre o codigo. */
    IPage<Materia> queryMateriaTable(QueryDto queryDto);

    Materia getMateria(Long id);

    /** Crea si id es null, edita si trae id. */
    void editMateria(Materia materia);

    void delMateria(Long id);
}
