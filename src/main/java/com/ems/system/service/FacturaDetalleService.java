package com.ems.system.service;

import com.ems.system.entity.FacturaDetalle;

import java.util.List;

public interface FacturaDetalleService {

    List<FacturaDetalle> findByFacturaId(Long facturaId);

    void deleteByFacturaId(Long facturaId);
}
