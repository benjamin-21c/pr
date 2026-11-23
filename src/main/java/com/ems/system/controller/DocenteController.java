package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.DocenteDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar docentes.
 * El frontend llama a /api/sys/docente/... y el filtro JWT lo reenvia a /sys/...
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class DocenteController extends ResultUtil {

    private final DocenteService docenteService;

    @Log("Consultar lista de docentes")
    @GetMapping("/docente/table")
    public ResponseEntity<Object> queryDocenteTable(QueryDto queryDto) {
        try {
            return success(docenteService.queryDocenteTable(queryDto));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Consultar docente por id")
    @GetMapping("/docente/info")
    public ResponseEntity<Object> getDocente(Long id) {
        try {
            return success(docenteService.getDocente(id));
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Crear o editar docente")
    @PostMapping("/docente/edit")
    public ResponseEntity<Object> editDocente(@RequestBody DocenteDto docenteDto) {
        try {
            String str = StringUtil.getEditType(docenteDto.getId());
            docenteService.editDocente(docenteDto);
            return success(str);
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }

    @Log("Eliminar docente")
    @DeleteMapping("/docente/del")
    public ResponseEntity<Object> delDocente(Long id) {
        try {
            docenteService.delDocente(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException ex) {
            return fail(ex.getMsg());
        }
    }
}