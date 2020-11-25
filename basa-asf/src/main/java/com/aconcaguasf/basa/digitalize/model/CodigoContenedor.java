package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.util.List;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "elementos")
public class CodigoContenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name="posicion_id", insertable=false, updatable=false)
    private Posiciones posicion;


    private String ubicacionProvisoria;

    public Posiciones getPosicion() {
        return posicion;
    }

    public void setPosicion(Posiciones posicion) {
        this.posicion = posicion;
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

    public String getUbicacionProvisoria() {
        return ubicacionProvisoria;
    }

    public void setUbicacionProvisoria(String ubicacionProvisoria) {
        this.ubicacionProvisoria = ubicacionProvisoria;
    }
}
