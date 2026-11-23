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
@TableName(value = "grado", schema = "academico")
public class Grado {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("nivel_id")
    private Long nivelId;
    @TableField("numero")
    private Integer numero;
    @TableField("nombre")
    private String nombre;
}
