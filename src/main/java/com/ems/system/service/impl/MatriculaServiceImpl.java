package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.AsignacionDocente;
import com.ems.system.entity.Curso;
import com.ems.system.entity.Estudiante;
import com.ems.system.entity.Matricula;
import com.ems.system.entity.MatriculaMateria;
import com.ems.system.entity.Persona;
import com.ems.system.entity.Calificacion;
import com.ems.system.entity.dto.MatriculaDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.AsignacionDocenteMapper;
import com.ems.system.mapper.CalificacionMapper;
import com.ems.system.mapper.CursoMapper;
import com.ems.system.mapper.EstudianteMapper;
import com.ems.system.mapper.MatriculaMapper;
import com.ems.system.mapper.MatriculaMateriaMapper;
import com.ems.system.mapper.PersonaMapper;
import com.ems.system.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Al crear una matricula se generan automaticamente sus matricula_materia
 * (una por cada asignacion docente vigente del curso).
 */
@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl extends ServiceImpl<MatriculaMapper, Matricula>
        implements MatriculaService {

    private static final List<String> ESTADOS = Arrays.asList("ACTIVA", "RETIRADA", "CONCLUIDA");

    private final EstudianteMapper estudianteMapper;
    private final PersonaMapper personaMapper;
    private final CursoMapper cursoMapper;
    private final AsignacionDocenteMapper asignacionDocenteMapper;
    private final MatriculaMateriaMapper matriculaMateriaMapper;
    private final CalificacionMapper calificacionMapper;

    @Override
    public IPage<MatriculaDto> queryMatriculaTable(QueryDto queryDto) {
        LambdaQueryWrapper<Matricula> wrapper = new LambdaQueryWrapper<>();
        String blurry = queryDto.getBlurry();
        if (StringUtils.hasText(blurry)) {
            List<Long> personaIds = personaMapper.selectList(new LambdaQueryWrapper<Persona>()
                            .like(Persona::getNombres, blurry)
                            .or().like(Persona::getApellidoPaterno, blurry)
                            .or().like(Persona::getApellidoMaterno, blurry)
                            .or().like(Persona::getDocumento, blurry))
                    .stream().map(Persona::getId).collect(Collectors.toList());
            LambdaQueryWrapper<Estudiante> ew = new LambdaQueryWrapper<>();
            ew.and(w -> {
                w.like(Estudiante::getCodigoEstudiante, blurry);
                if (!personaIds.isEmpty()) {
                    w.or().in(Estudiante::getPersonaId, personaIds);
                }
            });
            List<Long> estudianteIds = estudianteMapper.selectList(ew)
                    .stream().map(Estudiante::getId).collect(Collectors.toList());
            wrapper.and(w -> {
                w.like(Matricula::getEstado, blurry.toUpperCase());
                if (!estudianteIds.isEmpty()) {
                    w.or().in(Matricula::getEstudianteId, estudianteIds);
                }
            });
        }
        wrapper.orderByDesc(Matricula::getId);
        return this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper)
                .convert(this::toDto);
    }

    @Override
    public MatriculaDto getMatricula(Long id) {
        Matricula matricula = this.getById(id);
        if (matricula == null) {
            throw new BadRequestException("La matricula no existe");
        }
        return toDto(matricula);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editMatricula(MatriculaDto dto) {
        Matricula matricula = new Matricula();
        BeanUtils.copyProperties(dto, matricula);

        if (matricula.getEstudianteId() == null || matricula.getCursoId() == null) {
            throw new BadRequestException("Estudiante y curso son obligatorios");
        }
        if (estudianteMapper.selectById(matricula.getEstudianteId()) == null) {
            throw new BadRequestException("El estudiante no existe");
        }
        Curso curso = cursoMapper.selectById(matricula.getCursoId());
        if (curso == null) {
            throw new BadRequestException("El curso no existe");
        }
        // La gestion siempre es la del curso (matricula.gestion_id debe coincidir con curso.gestion_id)
        matricula.setGestionId(curso.getGestionId());

        if (StringUtils.hasText(matricula.getEstado())) {
            matricula.setEstado(matricula.getEstado().trim().toUpperCase());
            if (!ESTADOS.contains(matricula.getEstado())) {
                throw new BadRequestException("El estado debe ser ACTIVA, RETIRADA o CONCLUIDA");
            }
        }
        // Si estado o fechaMatricula vienen null no se insertan y la base aplica sus valores por defecto

        // UNIQUE(estudiante_id, gestion_id): una matricula por estudiante y gestion
        LambdaQueryWrapper<Matricula> dup = new LambdaQueryWrapper<Matricula>()
                .eq(Matricula::getEstudianteId, matricula.getEstudianteId())
                .eq(Matricula::getGestionId, matricula.getGestionId());
        if (matricula.getId() != null) {
            dup.ne(Matricula::getId, matricula.getId());
        }
        if (this.count(dup) > 0) {
            throw new BadRequestException("El estudiante ya esta matriculado en esa gestion");
        }

        if (matricula.getId() == null) {
            crear(matricula, curso);
        } else {
            Matricula actual = this.getById(matricula.getId());
            if (actual == null) {
                throw new BadRequestException("La matricula no existe");
            }
            if (!actual.getEstudianteId().equals(matricula.getEstudianteId())
                    || !actual.getCursoId().equals(matricula.getCursoId())) {
                throw new BadRequestException(
                        "No se puede cambiar el estudiante ni el curso: marque la matricula como RETIRADA y cree otra");
            }
            this.updateById(matricula);
        }
    }

    private void crear(Matricula matricula, Curso curso) {
        // Control de cupo
        Number cupo = curso.getCupo();
        if (cupo != null) {
            long activos = this.count(new LambdaQueryWrapper<Matricula>()
                    .eq(Matricula::getCursoId, curso.getId())
                    .eq(Matricula::getEstado, "ACTIVA"));
            if (activos >= cupo.longValue()) {
                throw new BadRequestException("El curso no tiene cupos disponibles");
            }
        }
        this.save(matricula);

        // Una matricula_materia por cada asignacion vigente del curso
        List<AsignacionDocente> asignaciones = asignacionDocenteMapper.selectList(
                new LambdaQueryWrapper<AsignacionDocente>()
                        .eq(AsignacionDocente::getCursoId, curso.getId())
                        .eq(AsignacionDocente::getActivo, true));
        for (AsignacionDocente asignacion : asignaciones) {
            MatriculaMateria mm = new MatriculaMateria();
            mm.setMatriculaId(matricula.getId());
            mm.setAsignacionDocenteId(asignacion.getId());
            mm.setEstado("CURSANDO");
            matriculaMateriaMapper.insert(mm);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMatricula(Long id) {
        if (this.getById(id) == null) {
            throw new BadRequestException("La matricula no existe");
        }
        List<Long> mmIds = matriculaMateriaMapper.selectList(
                        new LambdaQueryWrapper<MatriculaMateria>().eq(MatriculaMateria::getMatriculaId, id))
                .stream().map(MatriculaMateria::getId).collect(Collectors.toList());
        if (!mmIds.isEmpty() && calificacionMapper.selectCount(
                new LambdaQueryWrapper<Calificacion>().in(Calificacion::getMatriculaMateriaId, mmIds)) > 0) {
            throw new BadRequestException(
                    "No se puede eliminar: la matricula tiene calificaciones. Cambie su estado a RETIRADA");
        }
        matriculaMateriaMapper.delete(
                new LambdaQueryWrapper<MatriculaMateria>().eq(MatriculaMateria::getMatriculaId, id));
        this.removeById(id);
    }

    @Override
    public Map<String, Object> getMatriculaSelect() {
        List<Estudiante> estudiantes = estudianteMapper.selectList(
                new LambdaQueryWrapper<Estudiante>().eq(Estudiante::getActivo, true));
        Map<Long, Persona> personas = estudiantes.isEmpty() ? Collections.<Long, Persona>emptyMap()
                : personaMapper.selectBatchIds(
                        estudiantes.stream().map(Estudiante::getPersonaId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Persona::getId, p -> p));

        List<Map<String, Object>> opciones = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            Persona p = personas.get(e.getPersonaId());
            Map<String, Object> op = new LinkedHashMap<>();
            op.put("id", e.getId());
            op.put("codigoEstudiante", e.getCodigoEstudiante());
            op.put("nombre", p == null ? "" : p.getNombres() + " " + p.getApellidoPaterno());
            opciones.add(op);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("estudiantes", opciones);
        data.put("cursos", cursoMapper.selectList(null));
        data.put("estados", ESTADOS);
        return data;
    }

    private MatriculaDto toDto(Matricula matricula) {
        MatriculaDto dto = new MatriculaDto();
        BeanUtils.copyProperties(matricula, dto);
        return dto;
    }
}