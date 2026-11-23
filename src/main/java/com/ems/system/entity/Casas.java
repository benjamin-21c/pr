package com.ems.system.entity;
 
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ems.common.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
import java.util.Date;
 
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "casas")
public class Casas extends BaseEntity {
 
    @TableId(
        value = "id_casa",
        type = IdType.AUTO
    )
    private Long idCasa;
 
    private String nombreCasa;
 
    private String direccion;
 
    private String ciudad;
 
    private Long cantidadPisos;
 
    private Date fechaConstruccion;
}

