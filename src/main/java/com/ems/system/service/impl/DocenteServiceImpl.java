package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Docente;
import com.ems.system.entity.Persona;
import com.ems.system.entity.dto.DocenteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.DocenteMapper;
import com.ems.system.mapper.PersonaMapper;
import com.ems.system.service.DocenteService;
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
 * Maneja persona + docente en una sola operacion.
 * DocenteDto debe traer los campos de persona con el mismo nombre que la entidad Persona.
 */
@Service
@RequiredArgsConstructor
public class DocenteServiceImpl extends ServiceImpl<DocenteMapper, Docente>
        implements DocenteService {

    private final PersonaMapper personaMapper;

    @Override
    public IPage<DocenteDto> queryDocenteTable(QueryDto queryDto) {
        LambdaQueryWrapper<Docente> wrapper = new LambdaQueryWrapper<>();
        String blurry = queryDto.getBlurry();
        if (StringUtils.hasText(blurry)) {
            List<Long> personaIds = personaMapper.selectList(new LambdaQueryWrapper<Persona>()
                            .like(Persona::getNombres, blurry)
                            .or().like(Persona::getApellidoPaterno, blurry)
                            .or().like(Persona::getApellidoMaterno, blurry)
                            .or().like(Persona::getDocumento, blurry))
                    .stream().map(Persona::getId).collect(Collectors.toList());
            wrapper.and(w -> {
                w.like(Docente::getRegistroDocente, blurry)
                        .or().like(Docente::getEspecialidad, blurry);
                if (!personaIds.isEmpty()) {
                    w.or().in(Docente::getPersonaId, personaIds);
                }
            });
        }
        wrapper.orderByAsc(Docente::getId);

        Page<Docente> page = this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper);
        Map<Long, Persona> personas = cargarPersonas(page.getRecords());
        return page.convert(d -> toDto(d, personas.get(d.getPersonaId())));
    }

    @Override
    public DocenteDto getDocente(Long id) {
        Docente docente = this.getById(id);
        if (docente == null) {
            throw new BadRequestException("El docente no existe");
        }
        return toDto(docente, personaMapper.selectById(docente.getPersonaId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDocente(DocenteDto dto) {
        Persona persona = new Persona();
        BeanUtils.copyProperties(dto, persona);
        Docente docente = new Docente();
        BeanUtils.copyProperties(dto, docente);

        Long personaId = null;
        if (dto.getId() != null) {
            Docente actual = this.getById(dto.getId());
            if (actual == null) {
                throw new BadRequestException("El docente no existe");
            }
            personaId = actual.getPersonaId();
        }
        persona.setId(personaId);
        docente.setPersonaId(personaId);

        if (!StringUtils.hasText(persona.getNombres())) {
            throw new BadRequestException("Los nombres son obligatorios");
        }
        if (!StringUtils.hasText(persona.getApellidoPaterno())) {
            throw new BadRequestException("El apellido paterno es obligatorio");
        }
        if (!StringUtils.hasText(persona.getDocumento())) {
            throw new BadRequestException("El documento de identidad es obligatorio");
        }
        if (!StringUtils.hasText(docente.getRegistroDocente())) {
            throw new BadRequestException("El registro de docente es obligatorio");
        }

        LambdaQueryWrapper<Persona> dupDoc = new LambdaQueryWrapper<Persona>()
                .eq(Persona::getDocumento, persona.getDocumento());
        if (personaId != null) {
            dupDoc.ne(Persona::getId, personaId);
        }
        if (personaMapper.selectCount(dupDoc) > 0) {
            throw new BadRequestException("Ya existe una persona con ese documento");
        }
        LambdaQueryWrapper<Docente> dupReg = new LambdaQueryWrapper<Docente>()
                .eq(Docente::getRegistroDocente, docente.getRegistroDocente());
        if (dto.getId() != null) {
            dupReg.ne(Docente::getId, dto.getId());
        }
        if (this.count(dupReg) > 0) {
            throw new BadRequestException("Ya existe un docente con ese registro");
        }

        if (dto.getId() == null) {
            if (docente.getActivo() == null) {
                docente.setActivo(Boolean.TRUE);
            }
            personaMapper.insert(persona);
            docente.setPersonaId(persona.getId());
            this.save(docente);
        } else {
            personaMapper.updateById(persona);
            this.updateById(docente);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDocente(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("El docente no existe");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("No se puede eliminar: el docente tiene asignaciones registradas");
        }
    }

    private Map<Long, Persona> cargarPersonas(List<Docente> docentes) {
        List<Long> ids = docentes.stream().map(Docente::getPersonaId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return personaMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(Persona::getId, p -> p));
    }

    private DocenteDto toDto(Docente docente, Persona persona) {
        DocenteDto dto = new DocenteDto();
        if (persona != null) {
            BeanUtils.copyProperties(persona, dto);
        }
        BeanUtils.copyProperties(docente, dto);
        return dto;
    }
}