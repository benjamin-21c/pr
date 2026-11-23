package com.ems.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@TableName(value = "foto_usuario", schema = "academico")
public class FotoUsuario {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("nombre_archivo")
    private String nombreArchivo;
    @TableField("ruta_relativa")
    private String rutaRelativa;
    @TableField("mime_type")
    private String mimeType;
    @TableField("tamano_bytes")
    private Integer tamanoBytes;
    @TableField("fecha_carga")
    private LocalDateTime fechaCarga;
}
