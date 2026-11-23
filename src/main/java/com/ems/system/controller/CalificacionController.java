package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.CalificacionDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar calificaciones.
 * El frontend llama a /api/sys/academico/calificacion/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class CalificacionController extends ResultUtil {

    private final CalificacionService calificacionService;

    @Log("Consultar lista de calificaciones")
    @GetMapping("/calificacion/table")
    public ResponseEntity<Object> queryCalificacionTable(QueryDto queryDto) {
        try {
            return success(calificacionService.queryCalificacionTable(queryDto));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Consultar calificacion por id")
    @GetMapping("/calificacion/info")
    public ResponseEntity<Object> getCalificacion(Long id) {
        try {
            return success(calificacionService.getCalificacion(id));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Registrar o editar calificacion")
    @PostMapping("/calificacion/edit")
    public ResponseEntity<Object> editCalificacion(@RequestBody CalificacionDto calificacionDto) {
        try {
            String str = StringUtil.getEditType(calificacionDto.getId());
            calificacionService.editCalificacion(calificacionDto);
            return success(str);
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Eliminar calificacion")
    @DeleteMapping("/calificacion/del")
    public ResponseEntity<Object> delCalificacion(Long id) {
        try {
            calificacionService.delCalificacion(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}
