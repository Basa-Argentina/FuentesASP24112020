package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "tipos_requerimiento")
public class TipoRequerimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer plazo;

    @Column(nullable = false)
    private String tipoMovimiento;

    @Column(name = "clienteAsp_id")
    private Integer clienteAsp;

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public Integer getClienteAsp() {return clienteAsp;}

    public void setClienteAsp(Integer clienteAsp) {this.clienteAsp = clienteAsp; }

}






