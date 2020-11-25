package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerModificarReq {

    Operaciones operacion;

    Integer cantidadImagenes;
    Integer cantidadImagenesPlanos;
    Integer fletes;
    Integer horasArchivistas;

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public Integer getCantidadImagenes() {
        return cantidadImagenes;
    }

    public void setCantidadImagenes(Integer cantidadImagenes) {
        this.cantidadImagenes = cantidadImagenes;
    }

    public Integer getCantidadImagenesPlanos() {
        return cantidadImagenesPlanos;
    }

    public void setCantidadImagenesPlanos(Integer cantidadImagenesPlanos) {
        this.cantidadImagenesPlanos = cantidadImagenesPlanos;
    }

    public Integer getFletes() {
        return fletes;
    }

    public void setFletes(Integer fletes) {
        this.fletes = fletes;
    }

    public Integer getHorasArchivistas() {
        return horasArchivistas;
    }

    public void setHorasArchivista(Integer horasArchivistas) {
        this.horasArchivistas = horasArchivistas;
    }
}