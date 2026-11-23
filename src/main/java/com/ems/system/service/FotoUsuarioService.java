package com.ems.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.system.entity.FotoUsuario;
import org.springframework.web.multipart.MultipartFile;

public interface FotoUsuarioService extends IService<FotoUsuario> {

    /**
     * Valida rol administrador, tipo (JPEG/PNG/WebP) y tamano; guarda el archivo y
     * los metadatos en academico.foto_usuario. Lanza BadRequestException si algo falla.
     */
    FotoUsuario uploadFoto(Long userId, MultipartFile file, String username);
}