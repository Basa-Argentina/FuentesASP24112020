package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "movimientos_remitos")
public class MovimientoRemito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String movimiento;

    @Column(name = "cantidad_anterior")
    private Long cantidadAnterior;

    @Column(name = "id_remito")
    private Long idRemito;

    @Column(name = "id_elemento")
    private Long idElemento;

    private Calendar fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public Long getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(Long cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public Long getIdRemito() {
        return idRemito;
    }

    public void setIdRemito(Long idRemito) {
        this.idRemito = idRemito;
    }

    public Long getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(Long idElemento) {
        this.idElemento = idElemento;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }
}
