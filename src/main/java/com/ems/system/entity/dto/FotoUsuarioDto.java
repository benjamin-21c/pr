package com.ems.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FotoUsuarioDto {
    private Long id;
    private Long userId;
    private String nombreArchivo;
    private String rutaRelativa;
    private String mimeType;
    private Integer tamanoBytes;
    private LocalDateTime fechaCarga;
}