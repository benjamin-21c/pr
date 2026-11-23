package com.ems.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ems.system.entity.Docente;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteMapper extends BaseMapper<Docente> {
    IPage<Docente> queryDocenteTable(Page<?> page, @Param("blurry") String blurry);
}