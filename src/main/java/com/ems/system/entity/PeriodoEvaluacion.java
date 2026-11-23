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
@TableName(value = "periodo_evaluacion", schema = "academico")
public class PeriodoEvaluacion {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("gestion_id")
    private Long gestionId;
    @TableField("nombre")
    private String nombre;
    @TableField("orden")
    private Integer orden;
    @TableField("fecha_inicio")
    private LocalDate fechaInicio;
    @TableField("fecha_fin")
    private LocalDate fechaFin;
}
