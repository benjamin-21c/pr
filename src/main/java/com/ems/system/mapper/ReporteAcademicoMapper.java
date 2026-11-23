package com.ems.system.mapper;

import com.ems.system.entity.dto.BoletinEstudianteDto;
import com.ems.system.entity.dto.MatriculaCursoDto;
import com.ems.system.entity.dto.NominaDocenteDto;
import com.ems.system.entity.dto.NotaDocenteDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Las sentencias SQL se implementan en ReporteAcademicoMapper.xml. */
@Repository
public interface ReporteAcademicoMapper {

    List<NominaDocenteDto> listarNomina(@Param("asignacionId") Long asignacionId);

    List<NotaDocenteDto> listarNotas(
        @Param("asignacionId") Long asignacionId,
        @Param("periodoId") Long periodoId
    );

    List<BoletinEstudianteDto> listarBoletin(
        @Param("estudianteId") Long estudianteId,
        @Param("gestion") Integer gestion
    );

    List<MatriculaCursoDto> listarMatricula(
        @Param("gestion") Integer gestion,
        @Param("unidadEducativaId") Long unidadEducativaId
    );

    /** 1 si la asignacion pertenece al docente, 0 si no. */
    int contarAsignacionDocente(
        @Param("asignacionId") Long asignacionId,
        @Param("docenteId") Long docenteId
    );
}