package com.aconcaguasf.basa.digitalize.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.repository.query.Param;

import java.util.Date;


/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConceptosFilter {


    private Long sucursal;

    private String concepto_id;

    private String cliente;

    private Integer date;

    private Integer page;

    private Integer size;

    private Date fechaDesde;

    private Date fechaHasta;

    public Long getSucursal() {
        return sucursal;
    }

    public void setSucursal(Long sucursal) {
        this.sucursal = sucursal;
    }

    public String getConcepto_id() {
        return concepto_id;
    }

    public void setConcepto_id(String concepto_id) {
        this.concepto_id = concepto_id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

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

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}