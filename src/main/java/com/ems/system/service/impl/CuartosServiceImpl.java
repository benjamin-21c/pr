package com.ems.system.service.impl;
 
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ems.system.entity.Cuartos;
import com.ems.system.mapper.CuartosMapper;
import com.ems.system.service.CuartosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class CuartosServiceImpl implements CuartosService {
 
    private final CuartosMapper cuartosMapper;
 
    @Override
    public List<Cuartos> findByCasaId(Long idCasa) {
 
        LambdaQueryWrapper<Cuartos> wrapper =
                new LambdaQueryWrapper<>();
 
        wrapper.eq(Cuartos::getIdCasa, idCasa)
               .orderByAsc(Cuartos::getIdCuarto);
 
        return cuartosMapper.selectList(wrapper);
    }
}


