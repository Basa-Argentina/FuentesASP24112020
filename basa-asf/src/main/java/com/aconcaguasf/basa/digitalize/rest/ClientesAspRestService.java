package com.aconcaguasf.basa.digitalize.rest;

import com.aconcaguasf.basa.digitalize.model.ClientesAsp;
import com.aconcaguasf.basa.digitalize.repository.ClientesAspRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes_asp")
public class ClientesAspRestService {

    @Autowired
    private ClientesAspRepository clientesAspRepository;


    @RequestMapping(value = "")
    public List<ClientesAsp> getClientesAsp() {
        return clientesAspRepository.findAll();
    }

}
