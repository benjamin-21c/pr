package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.UnidadEducativa;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.UnidadEducativaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar unidades educativas.
 * El frontend llama a /api/sys/unidad_educativa/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class UnidadEducativaController extends ResultUtil {

    private final UnidadEducativaService unidadEducativaService;

    @Log("Consultar lista de unidades educativas")
    @GetMapping("/unidad_educativa/table")
    public ResponseEntity<Object> queryUnidadEducativaTable(QueryDto queryDto) {
        try {
            return success(unidadEducativaService.queryUnidadEducativaTable(queryDto));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Consultar unidad educativa por id")
    @GetMapping("/unidad_educativa/info")
    public ResponseEntity<Object> getUnidadEducativa(Long id) {
        try {
            return success(unidadEducativaService.getUnidadEducativa(id));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Crear o editar unidad educativa")
    @PostMapping("/unidad_educativa/edit")
    public ResponseEntity<Object> editUnidadEducativa(@RequestBody UnidadEducativa unidadEducativa) {
        try {
            String str = StringUtil.getEditType(unidadEducativa.getId());
            unidadEducativaService.editUnidadEducativa(unidadEducativa);
            return success(str);
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Eliminar unidad educativa")
    @DeleteMapping("/unidad_educativa/del")
    public ResponseEntity<Object> delUnidadEducativa(Long id) {
        try {
            unidadEducativaService.delUnidadEducativa(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}
