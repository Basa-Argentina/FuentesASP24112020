package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "x_requerimiento_conceptoF")
public class RelacionReqConF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long requerimiento_id;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private Long usuario_id;

    @Column(nullable = false)
    private Boolean estado;

    @Column(nullable = false)
    private String conceptoFacturable_id;

    @Transient
    private LocalDate fechaString;


    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="conceptoFacturable_id", insertable=false, updatable=false)
    private ConceptosFacturables conceptoFacturable;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="requerimiento_id", insertable=false, updatable=false)
    private RequerimientoLite requerimientoLite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequerimiento_id() {
        return requerimiento_id;
    }

    public void setRequerimiento_id(Long requerimiento_id) {
        this.requerimiento_id = requerimiento_id;
    }

    public String getConceptoFacturable_id() {
        return conceptoFacturable_id;
    }

    public void setConceptoFacturable_id(String conceptoFacturable_id) {
        this.conceptoFacturable_id = conceptoFacturable_id;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ConceptosFacturables getConceptoFacturable() {
        return conceptoFacturable;
    }

    public void setConceptoFacturable(ConceptosFacturables conceptoFacturable) {
        this.conceptoFacturable = conceptoFacturable;
    }

    public RequerimientoLite getRequerimientoLite() {
        return requerimientoLite;
    }

    public void setRequerimientoLite(RequerimientoLite requerimientoLite) {
        this.requerimientoLite = requerimientoLite;
    }
    public String getFechaString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(getFecha());
    }

    public void setFechaString(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaString = LocalDate.parse(fecha, formatter);
    }
}



