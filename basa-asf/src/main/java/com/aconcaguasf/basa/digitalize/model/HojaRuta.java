package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "hojaRuta")
public class HojaRuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Date fecha_salida;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_estado", nullable = false)
    private EstadoHojaRuta estadoHojaRuta;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public EstadoHojaRuta getEstadoHojaRuta() {
        return estadoHojaRuta;
    }

    public void setEstadoHojaRuta(EstadoHojaRuta estadoHojaRuta) {
        this.estadoHojaRuta = estadoHojaRuta;
    }

}



