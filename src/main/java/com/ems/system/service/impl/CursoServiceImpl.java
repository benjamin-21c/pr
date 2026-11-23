package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Curso;
import com.ems.system.entity.Grado;
import com.ems.system.entity.Matricula;
import com.ems.system.entity.Paralelo;
import com.ems.system.entity.UnidadEducativa;
import com.ems.system.entity.dto.CursoDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.CursoMapper;
import com.ems.system.mapper.GestionMapper;
import com.ems.system.mapper.GradoMapper;
import com.ems.system.mapper.MatriculaMapper;
import com.ems.system.mapper.NivelMapper;
import com.ems.system.mapper.ParaleloMapper;
import com.ems.system.mapper.UnidadEducativaMapper;
import com.ems.system.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl extends ServiceImpl<CursoMapper, Curso> implements CursoService {

    private static final List<String> TURNOS = Arrays.asList("MAÑANA", "TARDE", "NOCHE");

    private final UnidadEducativaMapper unidadEducativaMapper;
    private final GestionMapper gestionMapper;
    private final NivelMapper nivelMapper;
    private final GradoMapper gradoMapper;
    private final ParaleloMapper paraleloMapper;
    private final MatriculaMapper matriculaMapper;

    @Override
    public IPage<CursoDto> queryCursoTable(QueryDto queryDto) {
        LambdaQueryWrapper<Curso> wrapper = new LambdaQueryWrapper<>();
        String blurry = queryDto.getBlurry();
        if (StringUtils.hasText(blurry)) {
            List<Long> gradoIds = ids(gradoMapper.selectList(
                    new LambdaQueryWrapper<Grado>().like(Grado::getNombre, blurry)), Grado::getId);
            List<Long> paraleloIds = ids(paraleloMapper.selectList(
                    new LambdaQueryWrapper<Paralelo>().like(Paralelo::getNombre, blurry)), Paralelo::getId);
            List<Long> unidadIds = ids(unidadEducativaMapper.selectList(
                    new LambdaQueryWrapper<UnidadEducativa>().like(UnidadEducativa::getNombre, blurry)),
                    UnidadEducativa::getId);
            wrapper.and(w -> {
                w.like(Curso::getTurno, blurry);
                if (!gradoIds.isEmpty()) {
                    w.or().in(Curso::getGradoId, gradoIds);
                }
                if (!paraleloIds.isEmpty()) {
                    w.or().in(Curso::getParaleloId, paraleloIds);
                }
                if (!unidadIds.isEmpty()) {
                    w.or().in(Curso::getUnidadEducativaId, unidadIds);
                }
            });
        }
        wrapper.orderByAsc(Curso::getId);
        return this.page(new Page<>(queryDto.getCurrentPage(), queryDto.getSize()), wrapper)
                .convert(this::toDto);
    }

    @Override
    public CursoDto getCurso(Long id) {
        Curso curso = this.getById(id);
        if (curso == null) {
            throw new BadRequestException("El curso no existe");
        }
        return toDto(curso);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCurso(CursoDto dto) {
        Curso curso = new Curso();
        BeanUtils.copyProperties(dto, curso);

        if (curso.getUnidadEducativaId() == null || curso.getGestionId() == null
                || curso.getGradoId() == null || curso.getParaleloId() == null) {
            throw new BadRequestException("Unidad educativa, gestion, grado y paralelo son obligatorios");
        }
        if (!StringUtils.hasText(curso.getTurno())) {
            throw new BadRequestException("El turno es obligatorio");
        }
        curso.setTurno(curso.getTurno().trim().toUpperCase());
        if (!TURNOS.contains(curso.getTurno())) {
            throw new BadRequestException("El turno debe ser MAÑANA, TARDE o NOCHE");
        }
        // Si cupo viene null no se inserta y la base aplica su valor por defecto (40)
        Number cupo = curso.getCupo();
        if (cupo != null && (cupo.intValue() < 1 || cupo.intValue() > 150)) {
            throw new BadRequestException("El cupo debe estar entre 1 y 150");
        }

        if (unidadEducativaMapper.selectById(curso.getUnidadEducativaId()) == null) {
            throw new BadRequestException("La unidad educativa no existe");
        }
        if (gestionMapper.selectById(curso.getGestionId()) == null) {
            throw new BadRequestException("La gestion no existe");
        }
        if (gradoMapper.selectById(curso.getGradoId()) == null) {
            throw new BadRequestException("El grado no existe");
        }
        if (paraleloMapper.selectById(curso.getParaleloId()) == null) {
            throw new BadRequestException("El paralelo no existe");
        }

        // UNIQUE(unidad_educativa_id, gestion_id, grado_id, paralelo_id, turno)
        LambdaQueryWrapper<Curso> dup = new LambdaQueryWrapper<Curso>()
                .eq(Curso::getUnidadEducativaId, curso.getUnidadEducativaId())
                .eq(Curso::getGestionId, curso.getGestionId())
                .eq(Curso::getGradoId, curso.getGradoId())
                .eq(Curso::getParaleloId, curso.getParaleloId())
                .eq(Curso::getTurno, curso.getTurno());
        if (curso.getId() != null) {
            dup.ne(Curso::getId, curso.getId());
        }
        if (this.count(dup) > 0) {
            throw new BadRequestException("Ya existe ese curso (unidad, gestion, grado, paralelo y turno)");
        }

        if (curso.getId() == null) {
            this.save(curso);
        } else {
            Curso actual = this.getById(curso.getId());
            if (actual == null) {
                throw new BadRequestException("El curso no existe");
            }
            // No se puede cambiar la gestion si ya hay matriculas (matricula.gestion_id debe coincidir)
            if (!actual.getGestionId().equals(curso.getGestionId())
                    && matriculaMapper.selectCount(new LambdaQueryWrapper<Matricula>()
                    .eq(Matricula::getCursoId, curso.getId())) > 0) {
                throw new BadRequestException("No se puede cambiar la gestion de un curso con matriculas");
            }
            // El cupo no puede quedar por debajo de los matriculados activos
            if (cupo != null) {
                long activos = matriculaMapper.selectCount(new LambdaQueryWrapper<Matricula>()
                        .eq(Matricula::getCursoId, curso.getId())
                        .eq(Matricula::getEstado, "ACTIVA"));
                if (activos > cupo.longValue()) {
                    throw new BadRequestException("El cupo no puede ser menor a los " + activos
                            + " estudiantes matriculados");
                }
            }
            this.updateById(curso);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCurso(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new BadRequestException("El curso no existe");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("No se puede eliminar: el curso tiene matriculas o asignaciones");
        }
    }

    @Override
    public Map<String, Object> getCursoSelect() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("unidades", unidadEducativaMapper.selectList(null));
        data.put("gestiones", gestionMapper.selectList(null));
        data.put("niveles", nivelMapper.selectList(null));
        data.put("grados", gradoMapper.selectList(null));
        data.put("paralelos", paraleloMapper.selectList(null));
        data.put("turnos", TURNOS);
        return data;
    }

    private CursoDto toDto(Curso curso) {
        CursoDto dto = new CursoDto();
        BeanUtils.copyProperties(curso, dto);
        return dto;
    }

    private <T> List<Long> ids(List<T> lista, Function<T, Long> getter) {
        return lista.stream().map(getter).collect(Collectors.toList());
    }
}
