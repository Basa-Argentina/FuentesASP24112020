package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;


/**
 * Created by acorrea on 03/07/2017.
 */

@Entity
@Table(name = "lecturas")
public class Lecturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Long elementos;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private String Tipo;

    @Column(nullable = false)
    private String estado_id;

    private Long remito_id;

    @OneToMany(mappedBy="lectura_id",cascade = CascadeType.ALL )
    private List<LecturasDetalles> lecturasDetallesList;

    public String getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(String estado_id) {
        this.estado_id = estado_id;
    }

    public List<LecturasDetalles> getLecturasDetallesList() {
        return lecturasDetallesList;
    }

    public void setLecturasDetallesList(List<LecturasDetalles> lecturasDetallesList) {
        this.lecturasDetallesList = lecturasDetallesList;
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

    public Long getElementos() {
        return elementos;
    }

    public void setElementos(Long elementos) {
        this.elementos = elementos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public Long getRemito_id() {
        return remito_id;
    }

    public void setRemito_id(Long remito_id) {
        this.remito_id = remito_id;
    }
}
