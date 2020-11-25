package com.aconcaguasf.basa.digitalize.dto.reporte;

/**
 * Created by acorrea on 23/10/2017.
 */
public class HojaRutaReporte{

    public String serie;
    public String fechaEntrega;
    public String cliente;
    public String tipoRequerimiento;
    public String direccionEntrega;
    public Integer cantidadElemento;
    public String solicitante;
    public Long idHojaRutaOpElemnt;
    public String observaciones;

    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public String getFechaEntrega() {
        return fechaEntrega;
    }
    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
    public Integer getCantidadElemento() {
        return cantidadElemento;
    }
    public void setCantidadElemento(Integer cantidadElemento) {
        this.cantidadElemento = cantidadElemento;
    }
    public String getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    public Long getIdHojaRutaOpElemnt() {
        return idHojaRutaOpElemnt;
    }
    public void setIdHojaRutaOpElemnt(Long idHojaRutaOpElemnt) {
        this.idHojaRutaOpElemnt = idHojaRutaOpElemnt;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


}
