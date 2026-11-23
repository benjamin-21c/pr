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
@TableName(value = "plan_materia", schema = "academico")
public class PlanMateria {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("unidad_educativa_id")
    private Long unidadEducativaId;
    @TableField("grado_id")
    private Long gradoId;
    @TableField("materia_id")
    private Long materiaId;
}