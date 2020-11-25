package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "remitos")
public class Remitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Integer cantidadElementos;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Date fechaEmision;

    @Column(nullable = false)
    private Date fechaEntrega;

    @Column(nullable = false)
    private String letraComprobante;

    @Column(nullable = false)
    private Long numero;

    @Column(nullable = false)
    private String numeroSinPrefijo;

    @Column(nullable = false)
    private String observacion;


    @Column(nullable = false)
    private String prefijo;

    @Column(nullable = false)
    private String tipoRemito;

    @Column(nullable = false)
    private Long clienteAsp_id;

    @Column(nullable = false)
    private Long clienteEmp_id;

    @Column(nullable = false)
    private Long depositoDestino_id;

    @Column(nullable = false)
    private Long depositoOrigen_id;

    @Column(nullable = false)
    private Long direccion_id;

    @Column(nullable = false)
    private Long empleado_id;

    @Column(nullable = false)
    private Long empresa_id;

    @Column(nullable = false)
    private Long serie_id;
    @Column(nullable = false)
    private Long sucursal_id;

    @Column(nullable = false)
    private Long tipoComprobante_id;

    @Column(nullable = false)
    private Long transporte_id;

    @Column(nullable = false)
    private Long usuario_id;

    @Column(nullable = false)
    private Date fechaImpresion;

    @Column(nullable = false)
    private String ingresoEgreso;

    @Column(nullable = false)
    private String empleadoSolicitante;

    @Column(nullable = false)
    private String fechaSolicitud;

    @Column(nullable = false)
    private String numRequerimiento;

    @Column(nullable = false)
    private String tipoRequerimiento;

    @Column(nullable = false)
    private String aviso;

    @Transient
    private Long requerimiento_id;

    public Long getRequerimiento_id() {
        if(getNumRequerimiento()!=null)
            this.requerimiento_id = Long.parseLong(getNumRequerimiento().substring(getNumRequerimiento().length()-7));
        return requerimiento_id;
    }


    public Long getDireccion_id() {
        return direccion_id;
    }

    public void setDireccion_id(Long direccion_id) {
        this.direccion_id = direccion_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(Integer cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getLetraComprobante() {
        return letraComprobante;
    }

    public void setLetraComprobante(String letraComprobante) {
        this.letraComprobante = letraComprobante;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getNumeroSinPrefijo() {
        return numeroSinPrefijo;
    }

    public void setNumeroSinPrefijo(String numeroSinPrefijo) {
        this.numeroSinPrefijo = numeroSinPrefijo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getTipoRemito() {
        return tipoRemito;
    }

    public void setTipoRemito(String tipoRemito) {
        this.tipoRemito = tipoRemito;
    }

    public Long getClienteAsp_id() {
        return clienteAsp_id;
    }

    public void setClienteAsp_id(Long clienteAsp_id) {
        this.clienteAsp_id = clienteAsp_id;
    }

    public Long getClienteEmp_id() {
        return clienteEmp_id;
    }

    public void setClienteEmp_id(Long clienteEmp_id) {
        this.clienteEmp_id = clienteEmp_id;
    }

    public Long getDepositoDestino_id() {
        return depositoDestino_id;
    }

    public void setDepositoDestino_id(Long depositoDestino_id) {
        this.depositoDestino_id = depositoDestino_id;
    }

    public Long getDepositoOrigen_id() {
        return depositoOrigen_id;
    }

    public void setDepositoOrigen_id(Long depositoOrigen_id) {
        this.depositoOrigen_id = depositoOrigen_id;
    }

    public Long getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(Long empleado_id) {
        this.empleado_id = empleado_id;
    }

    public Long getEmpresa_id() {
        return empresa_id;
    }

    public void setEmpresa_id(Long empresa_id) {
        this.empresa_id = empresa_id;
    }

    public Long getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(Long serie_id) {
        this.serie_id = serie_id;
    }

    public Long getSucursal_id() {
        return sucursal_id;
    }

    public void setSucursal_id(Long sucursal_id) {
        this.sucursal_id = sucursal_id;
    }

    public Long getTipoComprobante_id() {
        return tipoComprobante_id;
    }

    public void setTipoComprobante_id(Long tipoComprobante_id) {
        this.tipoComprobante_id = tipoComprobante_id;
    }

    public Long getTransporte_id() {
        return transporte_id;
    }

    public void setTransporte_id(Long transporte_id) {
        this.transporte_id = transporte_id;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Date getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion(Date fechaImpresion) {
        this.fechaImpresion = fechaImpresion;
    }

    public String getIngresoEgreso() {
        return ingresoEgreso;
    }

    public void setIngresoEgreso(String ingresoEgreso) {
        this.ingresoEgreso = ingresoEgreso;
    }

    public String getEmpleadoSolicitante() {
        return empleadoSolicitante;
    }

    public void setEmpleadoSolicitante(String empleadoSolicitante) {
        this.empleadoSolicitante = empleadoSolicitante;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getNumRequerimiento() {
        return numRequerimiento;
    }

    public void setNumRequerimiento(String numRequerimiento) {
        this.numRequerimiento = numRequerimiento;
    }

    public String getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(String tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public String getAviso() { return aviso; }

    public void setAviso(String aviso) { this.aviso = aviso; }
}





