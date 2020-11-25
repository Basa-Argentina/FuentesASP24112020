package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

@Entity
@Table(name = "x_requerimiento_referencia")
public class ReqRef {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String tipoTrabajo;

    @Column(nullable = false)
    private Long requerimiento_id;

    @ManyToOne
    @JoinColumn(name="elemento_id", insertable=false, updatable=false)
    private Elementos elementos;

    public Elementos getElementos() {
        return elementos;
    }

    public void setElementos(Elementos elementos) {
        this.elementos = elementos;
    }

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

    public String getTipoTrabajo() {
        return tipoTrabajo;
    }

    public void setTipoTrabajo(String tipoTrabajo) {
        this.tipoTrabajo = tipoTrabajo;
    }
}
