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
@TableName(value = "unidad_educativa", schema = "academico")
public class UnidadEducativa {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("codigo")
    private String codigo;
    @TableField("nombre")
    private String nombre;
    @TableField("municipio")
    private String municipio;
    @TableField("distrito")
    private String distrito;
    @TableField("direccion")
    private String direccion;
    @TableField("activo")
    private Boolean activo;
}