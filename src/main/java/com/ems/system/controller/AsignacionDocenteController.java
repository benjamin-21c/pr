package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.AsignacionDocenteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.AsignacionDocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar asignaciones de docentes.
 * El frontend llama a /api/sys/asignacion/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class AsignacionDocenteController extends ResultUtil {

    private final AsignacionDocenteService asignacionDocenteService;

    @Log("Consultar lista de asignaciones de docentes")
    @GetMapping("/asignacion/table")
    public ResponseEntity<Object> queryAsignacionDocenteTable(QueryDto queryDto) {
        try {
            return success(asignacionDocenteService.queryAsignacionDocenteTable(queryDto));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Consultar asignacion de docente por id")
    @GetMapping("/asignacion/info")
    public ResponseEntity<Object> getAsignacionDocente(Long id) {
        try {
            return success(asignacionDocenteService.getAsignacionDocente(id));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Crear o editar asignacion de docente")
    @PostMapping("/asignacion/edit")
    public ResponseEntity<Object> editAsignacionDocente(@RequestBody AsignacionDocenteDto asignacionDocenteDto) {
        try {
            String str = StringUtil.getEditType(asignacionDocenteDto.getId());
            asignacionDocenteService.editAsignacionDocente(asignacionDocenteDto);
            return success(str);
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Eliminar asignacion de docente")
    @DeleteMapping("/asignacion/del")
    public ResponseEntity<Object> delAsignacionDocente(Long id) {
        try {
            asignacionDocenteService.delAsignacionDocente(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Opciones para el formulario de asignacion")
    @GetMapping("/asignacion/select")
    public ResponseEntity<Object> getAsignacionDocenteSelect() {
        try {
            // docentes, cursos y materias disponibles
            return success(asignacionDocenteService.getAsignacionDocenteSelect());
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }
}
