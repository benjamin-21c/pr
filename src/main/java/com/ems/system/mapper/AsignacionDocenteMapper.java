package com.ems.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.system.entity.AsignacionDocente;
import com.ems.system.entity.dto.AsignacionDocenteDto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AsignacionDocenteMapper extends BaseMapper<AsignacionDocente> {
List<AsignacionDocenteDto> listarAsignaciones(@Param("docenteId") Long docenteId, @Param("gestion") Integer gestion);
}