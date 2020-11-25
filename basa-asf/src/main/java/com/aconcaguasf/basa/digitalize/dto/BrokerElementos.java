package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerElementos {

    Operaciones operacion;

    List<String> elementosLeidos;

    Long tipoRequerimiento;
    boolean sinControl;

    public boolean isSinControl() {
        return sinControl;
    }

    public void setSinControl(boolean sinControl) {
        this.sinControl = sinControl;
    }

    public Long getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(Long tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public List<String> getElementosLeidos() {
        return elementosLeidos;
    }

    public void setElementosLeidos(List<String> elementosLeidos) {
        this.elementosLeidos = elementosLeidos;
    }

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public Long getSiguienteRequerimiento_id() {
        return tipoRequerimiento;
    }

    public void setSiguienteRequerimiento_id(Long siguienteRequerimiento_id) {
        this.tipoRequerimiento = siguienteRequerimiento_id;
    }
}