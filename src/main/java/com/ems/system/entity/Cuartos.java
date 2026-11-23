package com.ems.system.entity;
 
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ems.common.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
import java.math.BigDecimal;
 
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cuartos")
public class Cuartos extends BaseEntity {
 
    @TableId(
        value = "id_cuarto",
        type = IdType.AUTO
    )
    private Long idCuarto;
 
    private Long idCasa;
 
    private String nombreCuarto;
 
    private String tipoCuarto;
 
    private BigDecimal areaM2;
 
    private Boolean tieneVentana;
}
