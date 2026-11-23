package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.common.utils.StringUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.Materia;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar el catalogo de materias.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/academico")
public class MateriaController extends ResultUtil {

    private final MateriaService materiaService;

    @Log("Consultar lista de materias")
    @GetMapping("/materia/table")
    public ResponseEntity<Object> queryMateriaTable(QueryDto queryDto) {
        try {
            return success(materiaService.queryMateriaTable(queryDto));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Consultar materia por id")
    @GetMapping("/materia/info")
    public ResponseEntity<Object> getMateria(Long id) {
        try {
            return success(materiaService.getMateria(id));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Crear o editar materia")
    @PostMapping("/materia/edit")
    public ResponseEntity<Object> editMateria(@RequestBody Materia materia) {
        try {
            String str = StringUtil.getEditType(materia.getId());
            materiaService.editMateria(materia);
            return success(str);
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }

    @Log("Eliminar materia")
    @DeleteMapping("/materia/del")
    public ResponseEntity<Object> delMateria(Long id) {
        try {
            materiaService.delMateria(id);
            return success("Eliminacion exitosa");
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}