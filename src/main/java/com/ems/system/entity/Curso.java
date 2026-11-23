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
@TableName(value = "curso", schema = "academico")
public class Curso {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("unidad_educativa_id")
    private Long unidadEducativaId;
    @TableField("gestion_id")
    private Long gestionId;
    @TableField("grado_id")
    private Long gradoId;
    @TableField("paralelo_id")
    private Long paraleloId;
    @TableField("turno")
    private String turno;
    @TableField("cupo")
    private Integer cupo;
}
