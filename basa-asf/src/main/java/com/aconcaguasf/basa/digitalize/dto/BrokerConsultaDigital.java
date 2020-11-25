package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerConsultaDigital {

    Operaciones operacion;

    Integer hojas;

    Double horas;

    Integer elementos;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public Integer getHojas() {
        return hojas;
    }

    public void setHojas(Integer hojas) {
        this.hojas = hojas;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public Integer getElementos() {
        return elementos;
    }

    public void setElementos(Integer elementos) {
        this.elementos = elementos;
    }
}