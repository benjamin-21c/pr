package com.ems.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor 
@TableName(value = "persona", schema = "academico")
public class Persona {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField ("user_id")
    private Long userId;
    @TableField ("nombres")
    private String nombres;
    @TableField ("apellido_paterno")
    private String apellidoPaterno;
    @TableField ("apellido_materno")
    private String apellidoMaterno;
    @TableField ("documento")
    private String documento;
    @TableField ("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @TableField ("genero")
    private String genero;
    @TableField ("telefono")
    private String telefono;
    @TableField ("direccion")
    private String direccion;
}