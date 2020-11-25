package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.UsersxGrupo;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerRemitos {

String observaciones;

Operaciones operacion;

String tipoEnvio;

Boolean facturable;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getFacturable() {
        return facturable;
    }

    public void setFacturable(Boolean facturable) {
        this.facturable = facturable;
    }
}