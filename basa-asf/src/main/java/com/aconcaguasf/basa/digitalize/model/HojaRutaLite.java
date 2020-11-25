package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "hojaRuta")
public class HojaRutaLite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String estado;


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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}



