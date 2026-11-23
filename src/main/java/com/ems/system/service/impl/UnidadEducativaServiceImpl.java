package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.UnidadEducativa;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.UnidadEducativaMapper;
import com.ems.system.service.UnidadEducativaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UnidadEducativaServiceImpl
        extends ServiceImpl<UnidadEducativaMapper, UnidadEducativa>
        implements UnidadEducativaService {

    @Override
    public IPage<UnidadEducativa> queryUnidadEducativaTable(QueryDto queryDto) {
        LambdaQueryWrapper<UnidadEducativa> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDto.getBlurry())) {
            wrapper.and(w -> w.like(UnidadEducativa::getNombre, queryDto.getBlurry())
                    .or()
                    .like(UnidadEducativa::getCodigo, queryDto.getBlurry()));
        }
        wrapper.orderByAsc(UnidadEducativa::getId);
        return this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper);
    }

    @Override
    public UnidadEducativa getUnidadEducativa(Long id) {
        UnidadEducativa unidad = this.getById(id);
        if (unidad == null) {
            throw new BadRequestException("La unidad educativa no existe");
        }
        return unidad;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUnidadEducativa(UnidadEducativa unidad) {
        if (!StringUtils.hasText(unidad.getCodigo())) {
            throw new BadRequestException("El codigo es obligatorio");
        }
        if (!StringUtils.hasText(unidad.getNombre())) {
            throw new BadRequestException("El nombre es obligatorio");
        }

        // UNIQUE(codigo): validar antes de que lo rechace la base de datos
        LambdaQueryWrapper<UnidadEducativa> dup = new LambdaQueryWrapper<UnidadEducativa>()
                .eq(UnidadEducativa::getCodigo, unidad.getCodigo());
        if (unidad.getId() != null) {
            dup.ne(UnidadEducativa::getId, unidad.getId());
        }
        if (this.count(dup) > 0) {
            throw new BadRequestException("Ya existe una unidad educativa con ese codigo");
        }

        // Valores por defecto de la tabla (municipio 'Tarija', activo TRUE)
        if (!StringUtils.hasText(unidad.getMunicipio())) {
            unidad.setMunicipio("Tarija");
        }
        if (unidad.getActivo() == null) {
            unidad.setActivo(Boolean.TRUE);
        }

        if (unidad.getId() == null) {
            this.save(unidad);
        } else {
            this.updateById(unidad);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUnidadEducativa(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("La unidad educativa no existe");
            }
        } catch (DataIntegrityViolationException e) {
            // FK desde curso y plan_materia
            throw new BadRequestException("No se puede eliminar: tiene cursos o plan de materias asociados");
        }
    }
}
