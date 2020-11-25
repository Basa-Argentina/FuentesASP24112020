package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.ClientesAsp;

import java.util.Optional;

public interface ClienteAspService {

    Optional<ClientesAsp> getUserByUsername(String clienteAsp);

    void subscribe(ClientesAsp clientesAsp);

    ClientesAsp findById(Long id);

}