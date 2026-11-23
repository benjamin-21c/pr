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
@TableName(value = "nivel", schema = "academico")
public class Nivel {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("nombre")
    private String nombre;
    @TableField("codigo")
    private String codigo;
}