package com.ems.system.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Casas;
import com.ems.system.entity.Cuartos;
import com.ems.system.entity.dto.CasasDto;
import com.ems.system.entity.dto.CuartosDto;
import com.ems.system.entity.dto.QueryDto;
import com.ems.system.mapper.CasasMapper;
import com.ems.system.service.CasasService;
import com.ems.system.service.CuartosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
import java.util.ArrayList;
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class CasasServiceImpl implements CasasService {
 
    private final CasasMapper casasMapper;
    private final CuartosService cuartosService;
 
    @Override
    public IPage<Casas> queryCasasTable(QueryDto queryDto) {
        Page<Casas> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
 
        return casasMapper.queryCasasTable(
                page,
                queryDto.getBlurry()
        );
    }
    @Override
    public CasasDto getCasa(Long idCasa) {
 
        // 1. Validar el parametro recibido.
        if (idCasa == null) {
            throw new BadRequestException(
                    "El idCasa es obligatorio"
            );
        }
 
        // 2. Buscar la cabecera de la casa.
        Casas casa = casasMapper.selectById(idCasa);
 
        // 3. Validar que la casa exista.
        if (casa == null) {
            throw new BadRequestException(
                    "La casa no existe"
            );
        }
 
        // 4. Crear el DTO de salida.
        CasasDto casasDto = new CasasDto();
        casasDto.setIdCasa(casa.getIdCasa());
        casasDto.setNombreCasa(casa.getNombreCasa());
        casasDto.setDireccion(casa.getDireccion());
        casasDto.setCiudad(casa.getCiudad());
        casasDto.setCantidadPisos(casa.getCantidadPisos());
        casasDto.setFechaConstruccion(
                casa.getFechaConstruccion()
        );
 
        // 5. Consultar los cuartos asociados a la casa.
        List<Cuartos> cuartos =
                cuartosService.findByCasaId(idCasa);
 
        // 6. Convertir Cuartos a CuartosDto.
        List<CuartosDto> cuartosDtos =
                new ArrayList<>();
 
        for (Cuartos cuarto : cuartos) {
 
            CuartosDto dto = new CuartosDto();
            dto.setIdCuarto(cuarto.getIdCuarto());
            dto.setIdCasa(cuarto.getIdCasa());
            dto.setNombreCuarto(
                    cuarto.getNombreCuarto()
            );
            dto.setTipoCuarto(cuarto.getTipoCuarto());
            dto.setAreaM2(cuarto.getAreaM2());
            dto.setTieneVentana(
                    cuarto.getTieneVentana()
            );
 
            cuartosDtos.add(dto);
        }
 
        // 7. Incorporar el detalle al DTO principal.
        casasDto.setCuartos(cuartosDtos);
 
        return casasDto;
    }
}
