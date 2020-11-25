package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerHdR {

    Operaciones operacion;

    String numeroHdR;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public String getNumeroHdR() {
        return numeroHdR;
    }

    public void setNumeroHdR(String numeroHdR) {
        this.numeroHdR = numeroHdR;
    }
}