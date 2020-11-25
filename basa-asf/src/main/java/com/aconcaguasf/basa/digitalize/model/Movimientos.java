package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table(name = "movimientos")
public class Movimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "fecha")
    private Calendar fecha;

    @Column(name = "tipoMovimiento")
    private String tipoMovimiento;

    @Column(name = "clienteAsp_id")
    private Long clienteAsp_id;

    @Column(name = "clienteEmpOrigenDestino_id")
    private Long clienteEmpOrigenDestino_id;

    @Column(name = "deposito_id")
    private Long deposito_id;

    @Column(name = "depositoOrigenDestino_id")
    private Long depositoOrigenDestino_id;

    @Column(name = "elemento_id")
    private Long elemento_id;

    @Column(name = "lectura_id")
    private Long lectura_id;

    @Column(name = "posicionOrigenDestino_id")
    private Long posicionOrigenDestino_id;

    @Column(name = "remito_id")
    private Long remito_id;

    @Column(name = "usuario_id")
    private Long usuario_id;

    @Column(name = "descripcionRemito")
    private String descripcionRemito;

    @Column(name = "estado")
    private String estado;

    @Column(name = "claseMovimiento")
    private String claseMovimiento;

    @Column(name = "estadoElemento")
    private String estadoElemento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "responsable_id")
    private Long responsable_id;

    @Column(name = "codigoCarga")
    private Long codigoCarga;

   public Movimientos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Long getClienteAsp_id() {
        return clienteAsp_id;
    }

    public void setClienteAsp_id(Long clienteAsp_id) {
        this.clienteAsp_id = clienteAsp_id;
    }

    public Long getClienteEmpOrigenDestino_id() {
        return clienteEmpOrigenDestino_id;
    }

    public void setClienteEmpOrigenDestino_id(Long clienteEmpOrigenDestino_id) {
        this.clienteEmpOrigenDestino_id = clienteEmpOrigenDestino_id;
    }

    public Long getDeposito_id() {
        return deposito_id;
    }

    public void setDeposito_id(Long deposito_id) {
        this.deposito_id = deposito_id;
    }

    public Long getDepositoOrigenDestino_id() {
        return depositoOrigenDestino_id;
    }

    public void setDepositoOrigenDestino_id(Long depositoOrigenDestino_id) {
        this.depositoOrigenDestino_id = depositoOrigenDestino_id;
    }

    public Long getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(Long elemento_id) {
        this.elemento_id = elemento_id;
    }

    public Long getLectura_id() {
        return lectura_id;
    }

    public void setLectura_id(Long lectura_id) {
        this.lectura_id = lectura_id;
    }

    public Long getPosicionOrigenDestino_id() {
        return posicionOrigenDestino_id;
    }

    public void setPosicionOrigenDestino_id(Long posicionOrigenDestino_id) {
        this.posicionOrigenDestino_id = posicionOrigenDestino_id;
    }

    public Long getRemito_id() {
        return remito_id;
    }

    public void setRemito_id(Long remito_id) {
        this.remito_id = remito_id;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getDescripcionRemito() {
        return descripcionRemito;
    }

    public void setDescripcionRemito(String descripcionRemito) {
        this.descripcionRemito = descripcionRemito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClaseMovimiento() {
        return claseMovimiento;
    }

    public void setClaseMovimiento(String claseMovimiento) {
        this.claseMovimiento = claseMovimiento;
    }

    public String getEstadoElemento() {
        return estadoElemento;
    }

    public void setEstadoElemento(String estadoElemento) {
        this.estadoElemento = estadoElemento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(Long responsable_id) {
        this.responsable_id = responsable_id;
    }

    public Long getCodigoCarga() {
        return codigoCarga;
    }

    public void setCodigoCarga(Long codigoCarga) {
        this.codigoCarga = codigoCarga;
    }




}
