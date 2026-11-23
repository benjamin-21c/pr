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
@TableName(value = "estudiante", schema = "academico")
public class Estudiante {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("persona_id")
    private Long personaId;
    @TableField("codigo_estudiante")
    private String codigoEstudiante;
    @TableField("fecha_ingreso")
    private LocalDate fechaIngreso;
    @TableField("activo")
    private Boolean activo;
}