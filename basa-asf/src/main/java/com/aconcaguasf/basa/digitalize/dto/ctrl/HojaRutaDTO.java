package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.Date;

public class HojaRutaDTO {
    private Long id;
    private Date fecha_salida;
    private String estado;
    private String numero;

    public HojaRutaDTO() {
    }

    public HojaRutaDTO(String numero) {
        this.numero = numero;
    }

    public HojaRutaDTO(Long id, String numero) {
        this.id = id;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
