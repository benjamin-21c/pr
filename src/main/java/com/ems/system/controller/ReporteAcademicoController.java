package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.service.ReporteAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Reportes academicos (solo lectura). El acceso por rol se define en SecurityConfiguration
 * (docente / estudiante / administracion) y cada metodo del Service vuelve a validar la identidad.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico/reporte")
public class ReporteAcademicoController extends ResultUtil {

    private final ReporteAcademicoService reporteAcademicoService;

    @Log("Reporte: materias del docente")
    @GetMapping("/docente/materias")
    public ResponseEntity<Object> getDocenteMaterias(Integer gestion, Principal principal) {
        try {
            return success(reporteAcademicoService.getDocenteMaterias(gestion, principal.getName()));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Reporte: nomina de estudiantes del docente")
    @GetMapping("/docente/nomina")
    public ResponseEntity<Object> getDocenteNomina(Long asignacionId, Principal principal) {
        try {
            return success(reporteAcademicoService.getDocenteNomina(asignacionId, principal.getName()));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Reporte: notas del docente")
    @GetMapping("/docente/notas")
    public ResponseEntity<Object> getDocenteNotas(Long asignacionId, Long periodoId, Principal principal) {
        try {
            return success(reporteAcademicoService.getDocenteNotas(asignacionId, periodoId, principal.getName()));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Reporte: boletin del estudiante")
    @GetMapping("/estudiante/boletin")
    public ResponseEntity<Object> getEstudianteBoletin(Integer gestion, Principal principal) {
        try {
            return success(reporteAcademicoService.getEstudianteBoletin(gestion, principal.getName()));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Reporte: matricula por curso (administracion)")
    @GetMapping("/direccion/matricula")
    public ResponseEntity<Object> getDireccionMatricula(Integer gestion, Long unidadEducativaId,
                                                        Principal principal) {
        try {
            return success(reporteAcademicoService.getDireccionMatricula(
                    gestion, unidadEducativaId, principal.getName()));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}