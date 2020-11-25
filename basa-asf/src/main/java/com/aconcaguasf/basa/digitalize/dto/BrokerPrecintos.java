package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerPrecintos {

    Operaciones operacion;

    Integer precintos;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public Integer getPrecintos() {
        return precintos;
    }

    public void setPrecintos(Integer precintos) {
        this.precintos = precintos;
    }
}