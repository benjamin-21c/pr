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
@TableName(value = "matricula", schema = "academico")
public class Matricula {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("estudiante_id")
    private Long estudianteId;
    @TableField("curso_id")
    private Long cursoId;
    @TableField("gestion_id")
    private Long gestionId;
    @TableField("fecha_matricula")
    private LocalDate fechaMatricula;
    @TableField("estado")
    private String estado;
}