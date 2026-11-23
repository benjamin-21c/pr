package com.ems.system.service.impl;

import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.dto.BoletinEstudianteDto;
import com.ems.system.entity.dto.AsignacionDocenteDto;
import com.ems.system.entity.dto.MatriculaCursoDto;
import com.ems.system.entity.dto.NominaDocenteDto;
import com.ems.system.entity.dto.NotaDocenteDto;
import com.ems.system.mapper.AsignacionDocenteMapper;
import com.ems.system.mapper.ReporteAcademicoMapper;
import com.ems.system.service.ReporteAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteAcademicoServiceImpl implements ReporteAcademicoService {

    private final ReporteAcademicoMapper reporteMapper;
    private final AsignacionDocenteMapper asignacionDocenteMapper;
    private final UsuarioAcademicoResolver resolver;

    @Override
    public List<AsignacionDocenteDto> getDocenteMaterias(Integer gestion, String username) {
        validarGestion(gestion, true);
        Long docenteId = docenteDe(username);
        // Reutiliza la consulta del modulo de designaciones del ingeniero
        return asignacionDocenteMapper.listarAsignaciones(docenteId, gestion);
    }

    @Override
    public List<NominaDocenteDto> getDocenteNomina(Long asignacionId, String username) {
        verificarAsignacionDelDocente(asignacionId, docenteDe(username));
        return reporteMapper.listarNomina(asignacionId);
    }

    @Override
    public List<NotaDocenteDto> getDocenteNotas(Long asignacionId, Long periodoId, String username) {
        verificarAsignacionDelDocente(asignacionId, docenteDe(username));
        return reporteMapper.listarNotas(asignacionId, periodoId);
    }

    @Override
    public List<BoletinEstudianteDto> getEstudianteBoletin(Integer gestion, String username) {
        validarGestion(gestion, false);
        UsuarioAcademico u = resolver.resolver(username);
        if (!u.esEstudiante()) {
            throw new BadRequestException("Este reporte es solo para estudiantes");
        }
        // El estudiante sale del usuario autenticado, nunca de un parametro
        return reporteMapper.listarBoletin(u.getEstudiante().getId(), gestion);
    }

    @Override
    public List<MatriculaCursoDto> getDireccionMatricula(Integer gestion, Long unidadEducativaId, String username) {
        validarGestion(gestion, false);
        UsuarioAcademico u = resolver.resolver(username);
        if (u.esDocente() || u.esEstudiante()) {
            throw new BadRequestException("Este reporte es solo para administracion");
        }
        return reporteMapper.listarMatricula(gestion, unidadEducativaId);
    }

    // ---------- validaciones ----------

    private void validarGestion(Integer gestion, boolean obligatoria) {
        if (gestion == null) {
            if (obligatoria) {
                throw new BadRequestException("La gestion academica es obligatoria");
            }
            return;
        }
        if (gestion < 2000 || gestion > 2100) {
            throw new BadRequestException("La gestion academica no es valida");
        }
    }

    private Long docenteDe(String username) {
        UsuarioAcademico u = resolver.resolver(username);
        if (!u.esDocente()) {
            throw new BadRequestException("Este reporte es solo para docentes");
        }
        return u.getDocente().getId();
    }

    private void verificarAsignacionDelDocente(Long asignacionId, Long docenteId) {
        if (asignacionId == null) {
            throw new BadRequestException("La asignacion es obligatoria");
        }
        if (reporteMapper.contarAsignacionDocente(asignacionId, docenteId) == 0) {
            throw new BadRequestException("No tiene acceso a esa asignacion");
        }
    }
}