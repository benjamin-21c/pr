package com.ems.system.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ems.system.entity.Casas;
import com.ems.system.entity.dto.CasasDto;
import com.ems.system.entity.dto.QueryDto;
 
public interface CasasService {
 
    IPage<Casas> queryCasasTable(QueryDto queryDto);
 
    CasasDto getCasa(Long idCasa);
}
