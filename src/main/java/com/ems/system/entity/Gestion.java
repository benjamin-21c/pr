package com.ems.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@TableName(value = "gestion", schema = "academico")
public class Gestion {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("anio")
    private Integer anio;
    @TableField("fecha_inicio")
    private LocalDate fechaInicio;
    @TableField("fecha_fin")
    private LocalDate fechaFin;
    @TableField("estado")
    private String estado;
}