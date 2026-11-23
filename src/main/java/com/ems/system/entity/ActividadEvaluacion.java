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

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@TableName(value = "actividad_evaluacion", schema = "academico")
public class ActividadEvaluacion {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("asignacion_docente_id")
    private Long asignacionDocenteId;
    @TableField("periodo_evaluacion_id")
    private Long periodoEvaluacionId;
    @TableField("nombre")
    private String nombre;
    @TableField("ponderacion")
    private BigDecimal ponderacion;
    @TableField("nota_maxima")
    private BigDecimal notaMaxima;
}
