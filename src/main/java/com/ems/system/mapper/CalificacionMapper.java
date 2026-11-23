package com.ems.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ems.system.entity.Calificacion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CalificacionMapper extends BaseMapper<Calificacion> {
IPage<Calificacion> queryCalificacionTable(Page<Calificacion> page, @Param("blurry") String blurry);
}