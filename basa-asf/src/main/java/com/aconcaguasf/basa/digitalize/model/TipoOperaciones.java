package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "tipos_operacion")
public class TipoOperaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Boolean generaOperacionAlCerrarse;

    @Column(nullable = false)
    private Long tipoOperacionSiguiente_id;

    @Column(nullable = false)
    private Long tipoRequerimiento_id;

    public Long getTipoRequerimiento_id() {
        return tipoRequerimiento_id;
    }

    public void setTipoRequerimiento_id(Long tipoRequerimiento_id) {
        this.tipoRequerimiento_id = tipoRequerimiento_id;
    }

    public Long getTipoOperacionSiguiente_id() {
        return tipoOperacionSiguiente_id;
    }

    public void setTipoOperacionSiguiente_id(Long tipoOperacionSiguiente_id) {
        this.tipoOperacionSiguiente_id = tipoOperacionSiguiente_id;
    }


    public Boolean getGeneraOperacionAlCerrarse() {
        return generaOperacionAlCerrarse;
    }

    public void setGeneraOperacionAlCerrarse(Boolean generaOperacionAlCerrarse) {
        this.generaOperacionAlCerrarse = generaOperacionAlCerrarse;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    }




