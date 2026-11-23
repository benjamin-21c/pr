package com.ems.system.mapper;
 
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ems.system.entity.Casas;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CasasMapper extends BaseMapper<Casas> {
 
    IPage<Casas> queryCasasTable(
            IPage<?> page,
            @Param("blurry") String blurry
    );
}
