package com.aconcaguasf.basa.digitalize.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OperacionesFilter {


    private Long sucursal;

    private String tipoRequerimiento;

    private String estado;

    private String cliente;

    private Integer date;

    private Integer page;

    private Integer size;

    private String num_requerimiento;

    private String codigo_elemento;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Long getSucursal() {
        return sucursal;
    }

    public void setSucursal(Long sucursal) {
        this.sucursal = sucursal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(String tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public String getNum_requerimiento() {
        return num_requerimiento;
    }

    public void setNum_requerimiento(String num_requerimiento) {
        this.num_requerimiento = num_requerimiento;
    }

    public String getCodigo_elemento() {
        return codigo_elemento;
    }

    public void setCodigo_elemento(String codigo_elemento) {
        this.codigo_elemento = codigo_elemento;
    }
}