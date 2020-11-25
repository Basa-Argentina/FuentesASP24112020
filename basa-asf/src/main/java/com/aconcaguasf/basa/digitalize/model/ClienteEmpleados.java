package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "clienteEmpleados")
public class ClienteEmpleados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String celular;

    @Column(nullable = false)
    private Long clienteEmp_id;

    public Long getClienteEmp_id() {
        return clienteEmp_id;
    }

    public void setClienteEmp_id(Long clienteEmp_id) {
        this.clienteEmp_id = clienteEmp_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}




