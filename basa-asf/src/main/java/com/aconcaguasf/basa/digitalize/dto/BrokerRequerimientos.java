package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerRequerimientos {

    Operaciones operacion;

    List<String> elementosLeidos;

    Long tipoRequerimiento;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public List<String> getElementosLeidos() {
        return elementosLeidos;
    }

    public void setElementosLeidos(List<String> elementosLeidos) {
        this.elementosLeidos = elementosLeidos;
    }

    public Long getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(Long tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }
}