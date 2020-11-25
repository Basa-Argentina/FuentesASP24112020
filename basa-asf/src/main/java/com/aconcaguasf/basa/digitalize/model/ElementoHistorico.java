package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "elementos_historico")
public class ElementoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accion;

    @Column
    private Long accion_id;

    private String codigoCliente;

    @Column(nullable = false)
    private String codigoElemento;

    @Column(nullable = false)
    private String codigoTipoElemento;

    @Column(nullable = false)
    private Date fechaHora;

    private String nombreCliente;

    @Column(nullable = false)
    private String nombreTipoElemento;

    private Long clienteAsp_id;

    private Long usuario_id;

    public ElementoHistorico() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccion_id() {
        return accion_id;
    }

    public void setAccion_id(Long accion_id) {
        this.accion_id = accion_id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCodigoElemento() {
        return codigoElemento;
    }

    public void setCodigoElemento(String codigoElemento) {
        this.codigoElemento = codigoElemento;
    }

    public String getCodigoTipoElemento() {
        return codigoTipoElemento;
    }

    public void setCodigoTipoElemento(String codigoTipoElemento) {
        this.codigoTipoElemento = codigoTipoElemento;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreTipoElemento() {
        return nombreTipoElemento;
    }

    public void setNombreTipoElemento(String nombreTipoElemento) {
        this.nombreTipoElemento = nombreTipoElemento;
    }

    public Long getClienteAsp_id() {
        return clienteAsp_id;
    }

    public void setClienteAsp_id(Long clienteAsp_id) {
        this.clienteAsp_id = clienteAsp_id;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }
}
