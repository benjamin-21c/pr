package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.MatriculaDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar matriculas.
 * El frontend llama a /api/sys/matricula/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class MatriculaController extends ResultUtil {

    private final MatriculaService matriculaService;

    @Log("Consultar lista de matriculas")
    @GetMapping("/matricula/table")
    public ResponseEntity<Object> queryMatriculaTable(QueryDto queryDto) {
        try {
            return success(matriculaService.queryMatriculaTable(queryDto));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Consultar matricula por id")
    @GetMapping("/matricula/info")
    public ResponseEntity<Object> getMatricula(Long id) {
        try {
            return success(matriculaService.getMatricula(id));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Crear o editar matricula")
    @PostMapping("/matricula/edit")
    public ResponseEntity<Object> editMatricula(@RequestBody MatriculaDto matriculaDto) {
        try {
            String str = StringUtil.getEditType(matriculaDto.getId());
            matriculaService.editMatricula(matriculaDto);
            return success(str);
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Eliminar matricula")
    @DeleteMapping("/matricula/del")
    public ResponseEntity<Object> delMatricula(Long id) {
        try {
            matriculaService.delMatricula(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Opciones para el formulario de matricula")
    @GetMapping("/matricula/select")
    public ResponseEntity<Object> getMatriculaSelect() {
        try {
            // estudiantes, cursos y materias del curso
            return success(matriculaService.getMatriculaSelect());
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }
}