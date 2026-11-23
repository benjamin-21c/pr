package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Calificacion;
import com.ems.system.entity.dto.CalificacionDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.CalificacionMapper;
import com.ems.system.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionMapper calificacionMapper;

    @Override
    public IPage<Calificacion> queryCalificacionTable(QueryDto queryDto) {
        Page<Calificacion> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return calificacionMapper.queryCalificacionTable(page, queryDto.getBlurry());
    }

    @Override
    public CalificacionDto getCalificacion(Long id) {
        Calificacion entidad = calificacionMapper.selectById(id);
        if (entidad == null) {
            throw new BadRequestException("La calificacion no existe");
        }
        CalificacionDto dto = new CalificacionDto();
        dto.setId(entidad.getId());
        dto.setMatriculaMateriaId(entidad.getMatriculaMateriaId());
        dto.setActividadEvaluacionId(entidad.getActividadEvaluacionId());
        dto.setNota(entidad.getNota());
        dto.setObservacion(entidad.getObservacion());
        dto.setRegistradoPor(entidad.getRegistradoPor());
        dto.setFechaRegistro(entidad.getFechaRegistro());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCalificacion(CalificacionDto dto) {
        if (dto == null || dto.getMatriculaMateriaId() == null || dto.getActividadEvaluacionId() == null || dto.getNota() == null) {
            throw new BadRequestException("Matricula materia, actividad de evaluacion y nota son obligatorios");
        }
        if (dto.getNota().compareTo(BigDecimal.ZERO) < 0 || dto.getNota().compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("La nota debe estar entre 0 y 100");
        }
        Calificacion entidad = new Calificacion();
        entidad.setId(dto.getId());
        entidad.setMatriculaMateriaId(dto.getMatriculaMateriaId());
        entidad.setActividadEvaluacionId(dto.getActividadEvaluacionId());
        entidad.setNota(dto.getNota());
        entidad.setObservacion(dto.getObservacion());
        entidad.setRegistradoPor(dto.getRegistradoPor());
        entidad.setFechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDateTime.now());

        if (entidad.getId() != null) {
            calificacionMapper.updateById(entidad);
        } else {
            calificacionMapper.insert(entidad);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCalificacion(Long id) {
        if (calificacionMapper.selectById(id) == null) {
            throw new BadRequestException("La calificacion no existe");
        }
        calificacionMapper.deleteById(id);
    }
}