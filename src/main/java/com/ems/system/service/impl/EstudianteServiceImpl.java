package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Estudiante;
import com.ems.system.entity.Persona;
import com.ems.system.entity.dto.EstudianteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.EstudianteMapper;
import com.ems.system.mapper.PersonaMapper;
import com.ems.system.service.EstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maneja persona + estudiante en una sola operacion.
 * EstudianteDto debe traer los campos de persona con el mismo nombre que la entidad Persona
 * (nombres, apellidoPaterno, apellidoMaterno, documento, fechaNacimiento, genero, telefono, direccion, userId).
 */
@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl extends ServiceImpl<EstudianteMapper, Estudiante>
        implements EstudianteService {

    private final PersonaMapper personaMapper;

    @Override
    public IPage<EstudianteDto> queryEstudianteTable(QueryDto queryDto) {
        LambdaQueryWrapper<Estudiante> wrapper = new LambdaQueryWrapper<>();
        String blurry = queryDto.getBlurry();
        if (StringUtils.hasText(blurry)) {
            List<Long> personaIds = personaMapper.selectList(new LambdaQueryWrapper<Persona>()
                            .like(Persona::getNombres, blurry)
                            .or().like(Persona::getApellidoPaterno, blurry)
                            .or().like(Persona::getApellidoMaterno, blurry)
                            .or().like(Persona::getDocumento, blurry))
                    .stream().map(Persona::getId).collect(Collectors.toList());
            wrapper.and(w -> {
                w.like(Estudiante::getCodigoEstudiante, blurry);
                if (!personaIds.isEmpty()) {
                    w.or().in(Estudiante::getPersonaId, personaIds);
                }
            });
        }
        wrapper.orderByAsc(Estudiante::getId);

        Page<Estudiante> page = this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper);
        Map<Long, Persona> personas = cargarPersonas(page.getRecords());
        return page.convert(e -> toDto(e, personas.get(e.getPersonaId())));
    }

    @Override
    public EstudianteDto getEstudiante(Long id) {
        Estudiante estudiante = this.getById(id);
        if (estudiante == null) {
            throw new BadRequestException("El estudiante no existe");
        }
        return toDto(estudiante, personaMapper.selectById(estudiante.getPersonaId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editEstudiante(EstudianteDto dto) {
        Persona persona = new Persona();
        BeanUtils.copyProperties(dto, persona);
        Estudiante estudiante = new Estudiante();
        BeanUtils.copyProperties(dto, estudiante);

        Long personaId = null;
        if (dto.getId() != null) {
            Estudiante actual = this.getById(dto.getId());
            if (actual == null) {
                throw new BadRequestException("El estudiante no existe");
            }
            personaId = actual.getPersonaId();
        }
        persona.setId(personaId);
        estudiante.setPersonaId(personaId);

        if (!StringUtils.hasText(persona.getNombres())) {
            throw new BadRequestException("Los nombres son obligatorios");
        }
        if (!StringUtils.hasText(persona.getApellidoPaterno())) {
            throw new BadRequestException("El apellido paterno es obligatorio");
        }
        if (!StringUtils.hasText(persona.getDocumento())) {
            throw new BadRequestException("El documento de identidad es obligatorio");
        }
        if (!StringUtils.hasText(estudiante.getCodigoEstudiante())) {
            throw new BadRequestException("El codigo de estudiante es obligatorio");
        }

        // UNIQUE(documento) en persona
        LambdaQueryWrapper<Persona> dupDoc = new LambdaQueryWrapper<Persona>()
                .eq(Persona::getDocumento, persona.getDocumento());
        if (personaId != null) {
            dupDoc.ne(Persona::getId, personaId);
        }
        if (personaMapper.selectCount(dupDoc) > 0) {
            throw new BadRequestException("Ya existe una persona con ese documento");
        }
        // UNIQUE(codigo_estudiante)
        LambdaQueryWrapper<Estudiante> dupCod = new LambdaQueryWrapper<Estudiante>()
                .eq(Estudiante::getCodigoEstudiante, estudiante.getCodigoEstudiante());
        if (dto.getId() != null) {
            dupCod.ne(Estudiante::getId, dto.getId());
        }
        if (this.count(dupCod) > 0) {
            throw new BadRequestException("Ya existe un estudiante con ese codigo");
        }

        if (dto.getId() == null) {
            if (estudiante.getActivo() == null) {
                estudiante.setActivo(Boolean.TRUE);
            }
            personaMapper.insert(persona);
            estudiante.setPersonaId(persona.getId());
            this.save(estudiante);
        } else {
            personaMapper.updateById(persona);
            this.updateById(estudiante);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delEstudiante(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("El estudiante no existe");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("No se puede eliminar: el estudiante tiene matriculas registradas");
        }
    }

    private Map<Long, Persona> cargarPersonas(List<Estudiante> estudiantes) {
        List<Long> ids = estudiantes.stream().map(Estudiante::getPersonaId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return personaMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(Persona::getId, p -> p));
    }

    /** Primero se copia persona y luego estudiante, para que id sea el del estudiante. */
    private EstudianteDto toDto(Estudiante estudiante, Persona persona) {
        EstudianteDto dto = new EstudianteDto();
        if (persona != null) {
            BeanUtils.copyProperties(persona, dto);
        }
        BeanUtils.copyProperties(estudiante, dto);
        return dto;
    }
}
