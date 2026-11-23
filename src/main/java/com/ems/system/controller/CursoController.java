package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.CursoDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar cursos.
 * El frontend llama a /api/sys/curso/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class CursoController extends ResultUtil {

    private final CursoService cursoService;

    @Log("Consultar lista de cursos")
    @GetMapping("/curso/table")
    public ResponseEntity<Object> queryCursoTable(QueryDto queryDto) {
        try {
            return success(cursoService.queryCursoTable(queryDto));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Consultar curso por id")
    @GetMapping("/curso/info")
    public ResponseEntity<Object> getCurso(Long id) {
        try {
            return success(cursoService.getCurso(id));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Crear o editar curso")
    @PostMapping("/curso/edit")
    public ResponseEntity<Object> editCurso(@RequestBody CursoDto cursoDto) {
        try {
            String str = StringUtil.getEditType(cursoDto.getId());
            cursoService.editCurso(cursoDto);
            return success(str);
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Eliminar curso")
    @DeleteMapping("/curso/del")
    public ResponseEntity<Object> delCurso(Long id) {
        try {
            cursoService.delCurso(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Opciones para el formulario de curso")
    @GetMapping("/curso/select")
    public ResponseEntity<Object> getCursoSelect() {
        try {
            // gestiones, niveles, grados, paralelos y unidades educativas
            return success(cursoService.getCursoSelect());
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }
}
