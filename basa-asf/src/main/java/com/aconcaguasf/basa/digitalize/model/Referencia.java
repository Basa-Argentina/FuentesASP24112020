package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "referencia")
public class Referencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Date fecha1;

    @Column(nullable = false)
    private Date fecha2;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Long numero1;

    @Column(nullable = false)
    private Long numero2;

    @Column(nullable = false)
    private String texto1;

    @Column(nullable = false)
    private String texto2;

    @Column(nullable = false)
    private Long elemento_id;

    @Column(nullable = false)
    private Long clasificacion_documental_id;

    @Column(nullable = false)
    private Long cImagenes;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha1() {
        return fecha1;
    }
    public void setFecha1(Date fecha1) {
        this.fecha1 = fecha1;
    }

    public Date getFecha2() {
        return fecha2;
    }
    public void setFecha2(Date fecha2) {
        this.fecha2 = fecha2;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getNumero1() {
        return numero1;
    }
    public void setNumero1(Long numero1) {
        this.numero1 = numero1;
    }

    public Long getNumero2() {
        return numero2;
    }
    public void setNumero2(Long numero2) {
        this.numero2 = numero2;
    }

    public String getTexto1() {
        return texto1;
    }
    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }
    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public Long getElemento_id() {
        return elemento_id;
    }
    public void setElemento_id(Long elemento_id) {
        this.elemento_id = elemento_id;
    }

    public Long getClasificacion_documental_id() {
        return clasificacion_documental_id;
    }
    public void setClasificacion_documental_id(Long clasificacion_documental_id) {
        this.clasificacion_documental_id = clasificacion_documental_id;
    }
    public Long getcImagenes() {
        return cImagenes;
    }
    public void setcImagenes(Long cImagenes) {
        this.cImagenes = cImagenes;
    }

}
