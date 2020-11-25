package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "depositos")
public class Depositos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Boolean depositoPropio;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private Float subDisponible;

    @Column(nullable = false)
    private Float subTotal;

    @Column(nullable = false)
    private Long direccion_id;

    @Column(nullable = false)
    private Long sucursal_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getDepositoPropio() {
        return depositoPropio;
    }

    public void setDepositoPropio(Boolean depositoPropio) {
        this.depositoPropio = depositoPropio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Float getSubDisponible() {
        return subDisponible;
    }

    public void setSubDisponible(Float subDisponible) {
        this.subDisponible = subDisponible;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Long getDireccion_id() {
        return direccion_id;
    }

    public void setDireccion_id(Long direccion_id) {
        this.direccion_id = direccion_id;
    }

    public Long getSucursal_id() {
        return sucursal_id;
    }

    public void setSucursal_id(Long sucursal_id) {
        this.sucursal_id = sucursal_id;
    }
}




