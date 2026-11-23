package com.ems.system.service;

import com.ems.system.entity.dto.BoletinEstudianteDto;
import com.ems.system.entity.dto.AsignacionDocenteDto;
import com.ems.system.entity.dto.MatriculaCursoDto;
import com.ems.system.entity.dto.NominaDocenteDto;
import com.ems.system.entity.dto.NotaDocenteDto;

import java.util.List;

/**
 * Reportes de solo lectura. Cada metodo recibe el username autenticado para validar la identidad:
 * docente (solo sus asignaciones), estudiante (solo lo suyo) y administracion (matricula).
 */
public interface ReporteAcademicoService {

    /** Materias designadas al docente autenticado en una gestion (anio). */
    List<AsignacionDocenteDto> getDocenteMaterias(Integer gestion, String username);

    List<NominaDocenteDto> getDocenteNomina(Long asignacionId, String username);

    List<NotaDocenteDto> getDocenteNotas(Long asignacionId, Long periodoId, String username);

    /** Boletin del estudiante autenticado; gestion (anio) es opcional. */
    List<BoletinEstudianteDto> getEstudianteBoletin(Integer gestion, String username);

    List<MatriculaCursoDto> getDireccionMatricula(Integer gestion, Long unidadEducativaId, String username);
}