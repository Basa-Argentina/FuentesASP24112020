package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "direcciones")
public class Direcciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String dpto;

    @Column(nullable = false)
    private String edificio;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String observaciones;

    @Column(nullable = false)
    private String piso;

    //Complete direction String Property

    @Transient
    private String direccionCompleta;

    public String getDireccionCompleta() {

        this.direccionCompleta = (calle!=null&&!calle.equals("")?calle + " ":"") +
                (numero!=null&&!numero.equals("")?" " +numero + ". ":"") +
                (edificio!=null&&!edificio.equals("")?edificio+ " ":"") +
                (dpto!=null&&!dpto.equals("")?dpto + " ":"") +
                (piso!=null&&!piso.equals("")?piso + ". " :"")+
                (observaciones!=null&&!observaciones.equals("")?observaciones:"");
        return direccionCompleta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }


}




