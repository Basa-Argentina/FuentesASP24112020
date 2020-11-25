package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "hojaRuta_operacionElemento")
public class HdRxOperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long hojaRuta_id;

    @Column(nullable = false)
    private String operacionElemento_id;

    @Column(nullable = false)
    private String estado;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="hojaRuta_id", insertable=false, updatable=false)
    private HojaRuta hojaRuta;

    public HojaRuta getHojaRuta() {
        return hojaRuta;
    }

    public void setHojaRuta(HojaRuta hojaRuta) {
        this.hojaRuta = hojaRuta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHojaRuta_id() {
        return hojaRuta_id;
    }

    public void setHojaRuta_id(Long hojaRuta_id) {
        this.hojaRuta_id = hojaRuta_id;
    }

    public String getOperacionElemento_id() {
        return operacionElemento_id;
    }

    public void setOperacionElemento_id(String operacionElemento_id) {
        this.operacionElemento_id = operacionElemento_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}



