package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.EstudianteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.EstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar estudiantes.
 * El frontend llama a /api/sys/estudiante/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class EstudianteController extends ResultUtil {

    private final EstudianteService estudianteService;

    @Log("Consultar lista de estudiantes")
    @GetMapping("/estudiante/table")
    public ResponseEntity<Object> queryEstudianteTable(QueryDto queryDto) {
        try {
            return success(estudianteService.queryEstudianteTable(queryDto));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Consultar estudiante por id")
    @GetMapping("/estudiante/info")
    public ResponseEntity<Object> getEstudiante(Long id) {
        try {
            return success(estudianteService.getEstudiante(id));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Crear o editar estudiante")
    @PostMapping("/estudiante/edit")
    public ResponseEntity<Object> editEstudiante(@RequestBody EstudianteDto estudianteDto) {
        try {
            String str = StringUtil.getEditType(estudianteDto.getId());
            estudianteService.editEstudiante(estudianteDto);
            return success(str);
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Eliminar estudiante")
    @DeleteMapping("/estudiante/del")
    public ResponseEntity<Object> delEstudiante(Long id) {
        try {
            estudianteService.delEstudiante(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }
}