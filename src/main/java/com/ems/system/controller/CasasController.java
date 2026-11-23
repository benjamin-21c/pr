package com.ems.system.controller;
import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.dto.CasasDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.service.CasasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class CasasController extends ResultUtil {
 
    private final CasasService casasService;
 
    @Log("Consultar lista de casas")
    @GetMapping("/casas/table")
    public ResponseEntity<Object> queryCasasTable(QueryDto queryDto) {
        try {
            return success(casasService.queryCasasTable(queryDto));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
 
    @Log("Consultar casa por Id")
    @GetMapping("/casas/info")
    public ResponseEntity<Object> getCasa(
            @RequestParam("idCasa") Long idCasa) {
        try {
            return success(casasService.getCasa(idCasa));
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}
