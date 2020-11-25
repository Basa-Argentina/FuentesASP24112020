package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.List;

public class RequerimientoHdrDTO {
    private Long idReq;
    private Long numReq;
    private Long cantElementos;
    private Long idTipoReq;
    private Long codigoCliente;
    private String razonSocialCliente;
    private List<ElementosDTO> elementos;
    private String descTipoReq;
    private String localidadCliente;
    private String provinciaCliente;
    private String direccionCliente;
    private String tipoOperacion;
    private String remito;
    private String tipoMovimiento;
    private String observaciones;
    private String solicitante;
    private String fechaEntrega;
    private String telefonoCliente;
    private String usuarioAsignado;

    public RequerimientoHdrDTO() {
    }

    public Long getIdReq() {
        return idReq;
    }
    public void setIdReq(Long idReq) {
        this.idReq = idReq;
    }
    public Long getNumReq() {
        return numReq;
    }
    public void setNumReq(Long numReq) {
        this.numReq = numReq;
    }
    public Long getCantElementos() {
        return cantElementos;
    }
    public void setCantElementos(Long cantElementos) {
        this.cantElementos = cantElementos;
    }
    public Long getIdTipoReq() {
        return idTipoReq;
    }
    public void setIdTipoReq(Long idTipoReq) {
        this.idTipoReq = idTipoReq;
    }
    public Long getCodigoCliente() {
        return codigoCliente;
    }
    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }
    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }
    public List<ElementosDTO> getElementos() {
        return elementos;
    }
    public void setElementos(List<ElementosDTO> elementos) {
        this.elementos = elementos;
    }
    public String getDescTipoReq() {
        return descTipoReq;
    }
    public void setDescTipoReq(String descTipoReq) {
        this.descTipoReq = descTipoReq;
    }
    public String getLocalidadCliente() {
        return localidadCliente;
    }
    public void setLocalidadCliente(String localidadCliente) {
        this.localidadCliente = localidadCliente;
    }
    public String getProvinciaCliente() {
        return provinciaCliente;
    }
    public void setProvinciaCliente(String provinciaCliente) {
        this.provinciaCliente = provinciaCliente;
    }
    public String getDireccionCliente() {
        return direccionCliente;
    }
    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }
    public String getTipoOperacion() {
        return tipoOperacion;
    }
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    public String getRemito() {
        return remito;
    }
    public void setRemito(String remito) {
        this.remito = remito;
    }
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    public String getFechaEntrega() {
        return fechaEntrega;
    }
    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    public String getTelefonoCliente() {
        return telefonoCliente;
    }
    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }
    public String getUsuarioAsignado() {
        return usuarioAsignado;
    }
    public void setUsuarioAsignado(String usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

}
