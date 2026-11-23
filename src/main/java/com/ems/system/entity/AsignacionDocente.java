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
@TableName(value = "asignacion_docente", schema = "academico")
public class AsignacionDocente {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("curso_id")
    private Long cursoId;
    @TableField("plan_materia_id")
    private Long planMateriaId;
    @TableField("docente_id")
    private Long docenteId;
    @TableField("activo")
    private Boolean activo;
}