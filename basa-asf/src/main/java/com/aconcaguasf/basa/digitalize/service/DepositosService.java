package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.Depositos;

import java.util.Optional;

public interface DepositosService {

    Optional<Depositos> getRequerimientoByEstado(String estado);

    void subscribe(Depositos requerimiento);

    Depositos findById(Long id);

}