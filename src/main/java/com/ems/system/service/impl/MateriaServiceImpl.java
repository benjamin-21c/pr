package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Materia;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.MateriaMapper;
import com.ems.system.service.MateriaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MateriaServiceImpl
        extends ServiceImpl<MateriaMapper, Materia>
        implements MateriaService {

    @Override
    public IPage<Materia> queryMateriaTable(QueryDto queryDto) {
        LambdaQueryWrapper<Materia> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDto.getBlurry())) {
            wrapper.and(w -> w.like(Materia::getNombre, queryDto.getBlurry())
                    .or()
                    .like(Materia::getCodigo, queryDto.getBlurry()));
        }
        wrapper.orderByAsc(Materia::getId);
        return this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper);
    }

    @Override
    public Materia getMateria(Long id) {
        Materia materia = this.getById(id);
        if (materia == null) {
            throw new BadRequestException("La materia no existe");
        }
        return materia;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editMateria(Materia materia) {
        if (!StringUtils.hasText(materia.getCodigo())) {
            throw new BadRequestException("El codigo es obligatorio");
        }
        if (!StringUtils.hasText(materia.getNombre())) {
            throw new BadRequestException("El nombre es obligatorio");
        }

        // UNIQUE(codigo)
        LambdaQueryWrapper<Materia> dup = new LambdaQueryWrapper<Materia>()
                .eq(Materia::getCodigo, materia.getCodigo());
        if (materia.getId() != null) {
            dup.ne(Materia::getId, materia.getId());
        }
        if (this.count(dup) > 0) {
            throw new BadRequestException("Ya existe una materia con ese codigo");
        }

        // Valor por defecto de la tabla (activo TRUE)
        if (materia.getActivo() == null) {
            materia.setActivo(Boolean.TRUE);
        }

        if (materia.getId() == null) {
            this.save(materia);
        } else {
            this.updateById(materia);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMateria(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("La materia no existe");
            }
        } catch (DataIntegrityViolationException e) {
            // FK desde plan_materia
            throw new BadRequestException("No se puede eliminar: la materia esta en el plan de algun grado");
        }
    }
}