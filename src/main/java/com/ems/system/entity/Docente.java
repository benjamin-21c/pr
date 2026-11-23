package com.ems.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@TableName(value = "docente", schema = "academico")
public class Docente {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("persona_id")
    private Long personaId;
    @TableField("registro_docente")
    private String registroDocente;
    @TableField("especialidad")
    private String especialidad;
    @TableField("activo")
    private Boolean activo;
}