package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "req_hoja_ruta")
public class RequerimientoHojaRuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_requerimiento", nullable = false)
    private Long idRequerimiento;

    @Column(name = "id_hoja_ruta",nullable = false)
    private Long idHojaRuta;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_estado", nullable = false)
    private EstadoReqHojaRuta estadoReqHojaRuta;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdRequerimiento() {
        return idRequerimiento;
    }
    public void setIdRequerimiento(Long idRequerimiento) {
        this.idRequerimiento = idRequerimiento;
    }
    public Long getIdHojaRuta() {
        return idHojaRuta;
    }
    public void setIdHojaRuta(Long idHojaRuta) {
        this.idHojaRuta = idHojaRuta;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public EstadoReqHojaRuta getEstadoReqHojaRuta() {
        return estadoReqHojaRuta;
    }
    public void setEstadoReqHojaRuta(EstadoReqHojaRuta estadoReqHojaRuta) {
        this.estadoReqHojaRuta = estadoReqHojaRuta;
    }
}
