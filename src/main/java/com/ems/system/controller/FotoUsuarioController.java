package com.ems.system.controller;

import com.ems.common.exception.BadRequestException;
import com.ems.common.utils.ResultUtil;
import com.ems.logs.annotation.Log;
import com.ems.system.entity.FotoUsuario;
import com.ems.system.service.FotoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Carga de fotografia de usuario (multipart/form-data: userId + file JPEG/PNG/WebP).
 * Solo administrador: la validacion de rol, tipo y tamano del archivo se hace en el Service.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/user")
public class FotoUsuarioController extends ResultUtil {

    private final FotoUsuarioService fotoUsuarioService;

    @Log("Subir fotografia de usuario")
    @PostMapping("/user/photo")
    public ResponseEntity<Object> uploadFoto(@RequestParam("userId") Long userId,
                                             @RequestParam("file") MultipartFile file,
                                             Principal principal) {
        try {
            FotoUsuario foto = fotoUsuarioService.uploadFoto(userId, file, principal.getName());
            // Solo se devuelve id y tipo; la ruta de almacenamiento no se expone
            Map<String, Object> data = new HashMap<>();
            data.put("id", foto.getId());
            data.put("mimeType", foto.getMimeType());
            return success(data);
        } catch (BadRequestException e) {
            return fail(e.getMsg());
        }
    }
}
