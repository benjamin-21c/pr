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
@TableName(value = "matricula_materia", schema = "academico")
public class MatriculaMateria {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("matricula_id")
    private Long matriculaId;
    @TableField("asignacion_docente_id")
    private Long asignacionDocenteId;
    @TableField("estado")
    private String estado;
}