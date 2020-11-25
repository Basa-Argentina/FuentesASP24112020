package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Set;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "x_operacion_elemento")
public class RelacionOpEl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String elemento_id;

    @Column(nullable = false)
    private String operacion_id;

    @Column(nullable = false)
    private Integer traspasado;

    @Column(nullable = false)
    private Integer facturado;

    @Column(nullable = false)
    private Integer provieneLectura;

    @Column(nullable = false)
    private Integer rearchivoDe_id;

    @Column(nullable = false)
    private String pathArchivoDigital;



    @Column(nullable = false)
    private String busquedaEnPlanta;

    @OneToMany(mappedBy="operacionElemento_id")
    private Set<HdRxOperacion> hdRxOperacion;

    @ManyToOne
    @JoinColumn(name="elemento_id", insertable=false, updatable=false)
    private CodigoElemento codigoElemento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_hoja_ruta")
    private EstadoElementoHojaRuta estadoElementoHojaRuta;

    public Integer getTraspasado() {
        return traspasado;
    }

    public void setTraspasado(Integer traspasado) {
        this.traspasado = traspasado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CodigoElemento getCodigoElemento() {
        return codigoElemento;
    }

    public void setCodigoElemento(CodigoElemento codigoElemento) {
        this.codigoElemento = codigoElemento;
    }

    public Set<HdRxOperacion> getHdRxOperacion() {
        return hdRxOperacion;
    }

    public void setHdRxOperacion(Set<HdRxOperacion> hdRxOperacion) {
        this.hdRxOperacion = hdRxOperacion;
    }

    public RelacionOpEl() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(String elemento_id) {
        this.elemento_id = elemento_id;
    }

    public String getOperacion_id() {
        return operacion_id;
    }

    public void setOperacion_id(String operacion_id) {
        this.operacion_id = operacion_id;
    }

    public Integer getTraspaso() {
        return traspasado;
    }

    public void setTraspaso(Integer traspasado) {
        this.traspasado = traspasado;
    }

    public Integer getFacturado() {
        return facturado;
    }

    public void setFacturado(Integer facturado) {
        this.facturado = facturado;
    }

    public Integer getProvieneLectura() {
        return provieneLectura;
    }

    public void setProvieneLectura(Integer provieneLectura) {
        this.provieneLectura = provieneLectura;
    }

    public Integer getRearchivoDe_id() {
        return rearchivoDe_id;
    }

    public void setRearchivoDe_id(Integer rearchivoDe_id) {
        this.rearchivoDe_id = rearchivoDe_id;
    }

    public String getPathArchivoDigital() {
        return pathArchivoDigital;
    }

    public void setPathArchivoDigital(String pathArchivoDigital) {
        this.pathArchivoDigital = pathArchivoDigital;
    }

    public EstadoElementoHojaRuta getEstadoElementoHojaRuta() {
        return estadoElementoHojaRuta;
    }

    public void setEstadoElementoHojaRuta(EstadoElementoHojaRuta estadoElementoHojaRuta) {
        this.estadoElementoHojaRuta = estadoElementoHojaRuta;
    }

    public String getBusquedaEnPlanta() {
        return busquedaEnPlanta;
    }

    public void setBusquedaEnPlanta(String busquedaEnPlanta) {
        this.busquedaEnPlanta = busquedaEnPlanta;
    }
}