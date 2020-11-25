package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "clientesDirecciones")
public class ClientesDirecciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private Long direccion_id;

    @Column(nullable = false)
    private Long localidadAux_id;

    @Column(nullable = false)
    private Long provinciaAux_id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="direccion_id", insertable=false, updatable=false)
    private Direcciones direcciones;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="localidadAux_id", insertable=false, updatable=false)
    private Localidades localidades;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="provinciaAux_id", insertable=false, updatable=false)
    private Provincias provincias;

    public Long getProvinciaAux_id() {
        return provinciaAux_id;
    }

    public void setProvinciaAux_id(Long provinciaAux_id) {
        this.provinciaAux_id = provinciaAux_id;
    }

    public Provincias getProvincias() {
        return provincias;
    }

    public void setProvincias(Provincias provincias) {
        this.provincias = provincias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDireccion_id() {
        return direccion_id;
    }

    public void setDireccion_id(Long direccion_id) {
        this.direccion_id = direccion_id;
    }

    public Long getLocalidadAux_id() {
        return localidadAux_id;
    }

    public void setLocalidadAux_id(Long localidadAux_id) {
        this.localidadAux_id = localidadAux_id;
    }

    public Direcciones getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Direcciones direcciones) {
        this.direcciones = direcciones;
    }

    public Localidades getLocalidades() {
        return localidades;
    }

    public void setLocalidades(Localidades localidades) {
        this.localidades = localidades;
    }
}




