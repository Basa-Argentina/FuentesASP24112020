package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerOperaciones {

    List<Operaciones> operaciones;

    List<Long> remitos;

    Double hdr;

    public List<Operaciones> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<Operaciones> operaciones) {
        this.operaciones = operaciones;
    }

    public List<Long> getRemitos() {
        return remitos;
    }

    public void setRemitos(List<Long> remitos) {
        this.remitos = remitos;
    }

    public Double getHdr() {
        return hdr;
    }

    public void setHdr(Double hdr) {
        this.hdr = hdr;
    }
}