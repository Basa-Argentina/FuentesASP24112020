package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.Requerimiento;

import java.util.Optional;

public interface RequerimientoService {

    Optional<Requerimiento> getRequerimientoByEstado(String estado);

    void subscribe(Requerimiento requerimiento);

    Requerimiento findById(Long id);

}