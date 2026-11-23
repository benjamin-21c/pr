package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.AsignacionDocente;
import com.ems.system.entity.Curso;
import com.ems.system.entity.Docente;
import com.ems.system.entity.Persona;
import com.ems.system.entity.PlanMateria;
import com.ems.system.entity.dto.AsignacionDocenteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.AsignacionDocenteMapper;
import com.ems.system.mapper.CursoMapper;
import com.ems.system.mapper.DocenteMapper;
import com.ems.system.mapper.MateriaMapper;
import com.ems.system.mapper.PersonaMapper;
import com.ems.system.mapper.PlanMateriaMapper;
import com.ems.system.service.AsignacionDocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignacionDocenteServiceImpl extends ServiceImpl<AsignacionDocenteMapper, AsignacionDocente>
        implements AsignacionDocenteService {

    private final DocenteMapper docenteMapper;
    private final PersonaMapper personaMapper;
    private final CursoMapper cursoMapper;
    private final PlanMateriaMapper planMateriaMapper;
    private final MateriaMapper materiaMapper;

    @Override
    public IPage<AsignacionDocenteDto> queryAsignacionDocenteTable(QueryDto queryDto) {
        LambdaQueryWrapper<AsignacionDocente> wrapper = new LambdaQueryWrapper<>();
        String blurry = queryDto.getBlurry();
        if (StringUtils.hasText(blurry)) {
            List<Long> docenteIds = docenteIdsPorTexto(blurry);
            if (docenteIds.isEmpty()) {
                wrapper.eq(AsignacionDocente::getId, -1L); // sin coincidencias
            } else {
                wrapper.in(AsignacionDocente::getDocenteId, docenteIds);
            }
        }
        wrapper.orderByAsc(AsignacionDocente::getId);
        return this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper)
                .convert(this::toDto);
    }

    @Override
    public AsignacionDocenteDto getAsignacionDocente(Long id) {
        AsignacionDocente asignacion = this.getById(id);
        if (asignacion == null) {
            throw new BadRequestException("La asignacion no existe");
        }
        return toDto(asignacion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editAsignacionDocente(AsignacionDocenteDto dto) {
        AsignacionDocente asignacion = new AsignacionDocente();
        BeanUtils.copyProperties(dto, asignacion);

        if (asignacion.getCursoId() == null || asignacion.getPlanMateriaId() == null
                || asignacion.getDocenteId() == null) {
            throw new BadRequestException("Curso, materia del plan y docente son obligatorios");
        }
        Curso curso = cursoMapper.selectById(asignacion.getCursoId());
        if (curso == null) {
            throw new BadRequestException("El curso no existe");
        }
        PlanMateria plan = planMateriaMapper.selectById(asignacion.getPlanMateriaId());
        if (plan == null) {
            throw new BadRequestException("La materia del plan no existe");
        }
        if (docenteMapper.selectById(asignacion.getDocenteId()) == null) {
            throw new BadRequestException("El docente no existe");
        }
        // La materia debe ser del mismo grado y establecimiento que el curso
        if (!plan.getGradoId().equals(curso.getGradoId())
                || !plan.getUnidadEducativaId().equals(curso.getUnidadEducativaId())) {
            throw new BadRequestException("La materia no pertenece al grado y unidad educativa del curso");
        }

        // UNIQUE(curso_id, plan_materia_id)
        LambdaQueryWrapper<AsignacionDocente> dup = new LambdaQueryWrapper<AsignacionDocente>()
                .eq(AsignacionDocente::getCursoId, asignacion.getCursoId())
                .eq(AsignacionDocente::getPlanMateriaId, asignacion.getPlanMateriaId());
        if (asignacion.getId() != null) {
            dup.ne(AsignacionDocente::getId, asignacion.getId());
        }
        if (this.count(dup) > 0) {
            throw new BadRequestException("Esa materia ya tiene docente asignado en ese curso");
        }

        if (asignacion.getId() == null) {
            if (asignacion.getActivo() == null) {
                asignacion.setActivo(Boolean.TRUE);
            }
            this.save(asignacion);
        } else {
            if (this.getById(asignacion.getId()) == null) {
                throw new BadRequestException("La asignacion no existe");
            }
            this.updateById(asignacion);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAsignacionDocente(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("La asignacion no existe");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(
                    "No se puede eliminar: la asignacion tiene estudiantes o actividades de evaluacion");
        }
    }

    @Override
    public Map<String, Object> getAsignacionDocenteSelect() {
        List<Docente> docentes = docenteMapper.selectList(
                new LambdaQueryWrapper<Docente>().eq(Docente::getActivo, true));
        Map<Long, Persona> personas = docentes.isEmpty() ? Collections.<Long, Persona>emptyMap()
                : personaMapper.selectBatchIds(
                        docentes.stream().map(Docente::getPersonaId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Persona::getId, p -> p));

        List<Map<String, Object>> opcionesDocente = new ArrayList<>();
        for (Docente d : docentes) {
            Persona p = personas.get(d.getPersonaId());
            Map<String, Object> op = new LinkedHashMap<>();
            op.put("id", d.getId());
            op.put("registroDocente", d.getRegistroDocente());
            op.put("nombre", p == null ? "" : p.getNombres() + " " + p.getApellidoPaterno());
            opcionesDocente.add(op);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("docentes", opcionesDocente);
        data.put("cursos", cursoMapper.selectList(null));
        data.put("planMaterias", planMateriaMapper.selectList(null));
        data.put("materias", materiaMapper.selectList(null));
        return data;
    }

    private List<Long> docenteIdsPorTexto(String texto) {
        List<Long> personaIds = personaMapper.selectList(new LambdaQueryWrapper<Persona>()
                        .like(Persona::getNombres, texto)
                        .or().like(Persona::getApellidoPaterno, texto)
                        .or().like(Persona::getApellidoMaterno, texto))
                .stream().map(Persona::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Docente> dw = new LambdaQueryWrapper<>();
        dw.and(w -> {
            w.like(Docente::getRegistroDocente, texto);
            if (!personaIds.isEmpty()) {
                w.or().in(Docente::getPersonaId, personaIds);
            }
        });
        return docenteMapper.selectList(dw).stream().map(Docente::getId).collect(Collectors.toList());
    }

    private AsignacionDocenteDto toDto(AsignacionDocente asignacion) {
        AsignacionDocenteDto dto = new AsignacionDocenteDto();
        BeanUtils.copyProperties(asignacion, dto);
        return dto;
    }
}
