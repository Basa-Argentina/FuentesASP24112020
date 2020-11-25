package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "remitosDetalle")
public class RemitosDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long orden;

    @Column(nullable = false)
    private Long elemento_id;

    @Column(nullable = false)
    private Long remito_id;

    @ManyToOne
    @JoinColumn(name="remito_id", insertable=false, updatable=false)
    private Remitos remitos;

    public Remitos getRemitos() {
        return remitos;
    }

    public void setRemitos2(Remitos remitos2) {
        this.remitos = remitos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public Long getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(Long elemento_id) {
        this.elemento_id = elemento_id;
    }

    public Long getRemito_id() {
        return remito_id;
    }

    public void setRemito_id(Long remito_id) {
        this.remito_id = remito_id;
    }
}





