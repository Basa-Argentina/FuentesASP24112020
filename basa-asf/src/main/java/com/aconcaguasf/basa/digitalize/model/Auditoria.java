package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private Long requerimiento_id;

    @Column(nullable = false)
    private Long tipoOperacion_id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private Long clienteEmp_id;

    public Long getClienteEmp_id() {
        return clienteEmp_id;
    }

    public void setClienteEmp_id(Long clienteEmp_id) {
        this.clienteEmp_id = clienteEmp_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getRequerimiento_id() {
        return requerimiento_id;
    }

    public void setRequerimiento_id(Long requerimiento_id) {
        this.requerimiento_id = requerimiento_id;
    }

    public Long getTipoOperacion_id() {
        return tipoOperacion_id;
    }

    public void setTipoOperacion_id(Long tipoOperacion_id) {
        this.tipoOperacion_id = tipoOperacion_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
