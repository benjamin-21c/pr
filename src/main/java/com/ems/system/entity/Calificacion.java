package com.ems.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@TableName(value = "calificacion", schema = "academico")
public class Calificacion {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("matricula_materia_id")
    private Long matriculaMateriaId;
    @TableField("actividad_evaluacion_id")
    private Long actividadEvaluacionId;
    @TableField("nota")
    private BigDecimal nota;
    @TableField("observacion")
    private String observacion;
    @TableField("registrado_por")
    private Long registradoPor;
    @TableField("fecha_registro")
    private LocalDateTime fechaRegistro;
}
