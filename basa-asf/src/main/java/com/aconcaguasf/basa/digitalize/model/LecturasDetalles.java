package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "lecturaDetalle")
public class LecturasDetalles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigoBarras;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private Long elemento_id;

    @Column(nullable = false)
    private Long lectura_id;

    @Column(nullable = false)
    private String estado_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(Long elemento_id) {
        this.elemento_id = elemento_id;
    }

    public Long getLectura_id() {
        return lectura_id;
    }

    public void setLectura_id(Long lectura_id) {
        this.lectura_id = lectura_id;
    }

    public String getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(String estado_id) {
        this.estado_id = estado_id;
    }
}
