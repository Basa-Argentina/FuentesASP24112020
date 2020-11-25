package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "elementos")
public class Elementos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Boolean generaCanonMensual;

    @Column(nullable = false)
    private Long clienteAsp_id;

    @Column(nullable = false)
    private Long clienteEmp_id;

    @Column(nullable = false)
    private Long contenedor_id;

    @Column(nullable = false)
    private Long tipoElemento_id;

    @Column(nullable = false)
    private Long depositoActual_id;

    @Column(nullable = false)
    private String codigoAlternativo;

    @Column(nullable = false)
    private Long nroPrecinto;

    @Column(nullable = false)
    private String estadoTrabajo;

    @Column(nullable = false)
    private Date fechaModificacion;

    @Column(nullable = false)
    private Date fechaModificacionPrecinto;

    @Column(nullable = false)
    private Long nroPrecinto1;

    @Column(nullable = false)
    private String tipoTrabajo;

    @Column(nullable = false)
    private Long usuarioModificacion_id;

    @Column(nullable = false)
    private Long usuarioModificacionPrecinto_id;

    @Column(nullable = false)
    private String ubicacionProvisoria;

    @Column(nullable = false)
    private Boolean cerrado;

    @Column(nullable = false)
    private Boolean habilitadoCerrar;

    @Column (nullable = false)
    private Long posicion_id;

    @ManyToOne
    @JoinColumn(name="tipoElemento_id", insertable=false, updatable=false)
    private TipoElemento tipoElemento;



    @ManyToOne
    @JoinColumn(name="posicion_id", insertable=false, updatable=false)
    private Posiciones posicion;

    @ManyToOne
    @JoinColumn(name="clienteEmp_id", insertable=false, updatable=false)
    private ClienteEmp clienteEmp;

    @OneToMany(mappedBy = "elemento_id", fetch = FetchType.EAGER)
    private Set<RelacionOpEl> relacionOpEl;

    public Set<RelacionOpEl> getRelacionOpEl() {

        return relacionOpEl;
    }

    public void setRelacionOpEl(Set<RelacionOpEl> relacionOpEl) {
        this.relacionOpEl = relacionOpEl;
    }

    @OneToMany(mappedBy = "elemento_id", fetch = FetchType.EAGER)
    private Set<Referencia> referencias;

    @ManyToOne
    @JoinColumn(name="contenedor_id", insertable=false, updatable=false)
    private CodigoContenedor codigoContenedor;

    public CodigoContenedor getCodigoContenedor() {
        return codigoContenedor;
    }

    public void setCodigoContenedor(CodigoContenedor codigoContenedor) {
        this.codigoContenedor = codigoContenedor;
    }

    public Set<Referencia> getReferencias() {
        List<String> tipoLegajos = asList("2", "4", "6", "8");
        if(tipoLegajos.contains(getTipoElemento_id().toString()))
        return referencias;
        else return null;
    }

    public void setReferencias(Set<Referencia> referencias) {
        this.referencias = referencias;
    }

    public ClienteEmp getClienteEmp() {
        return clienteEmp;
    }

    public void setClienteEmp(ClienteEmp clienteEmp) {
        this.clienteEmp = clienteEmp;
    }

    public Posiciones getPosicion() {
        return posicion;
    }

    public void setPosicion(Posiciones posicion) {
        this.posicion = posicion;
    }

    public Long getPosicion_id() {
        return posicion_id;
    }

    public void setPosicion_id(Long posicion_id) {
        this.posicion_id = posicion_id;
    }

    public TipoElemento getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(TipoElemento tipoElemento) {
        this.tipoElemento = tipoElemento;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getGeneraCanonMensual() {
        return generaCanonMensual;
    }

    public void setGeneraCanonMensual(Boolean generaCanonMensual) {
        this.generaCanonMensual = generaCanonMensual;
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

    public Long getContenedor_id() {
        return contenedor_id;
    }

    public void setContenedor_id(Long contenedor_id) {
        this.contenedor_id = contenedor_id;
    }

    public Long getTipoElemento_id() {
        return tipoElemento_id;
    }

    public void setTipoElemento_id(Long tipoElemento_id) {
        this.tipoElemento_id = tipoElemento_id;
    }

    public Long getDepositoActual_id() {
        return depositoActual_id;
    }

    public void setDepositoActual_id(Long depositoActual_id) {
        this.depositoActual_id = depositoActual_id;
    }

    public String getCodigoAlternativo() {
        return codigoAlternativo;
    }

    public void setCodigoAlternativo(String codigoAlternativo) {
        this.codigoAlternativo = codigoAlternativo;
    }

    public Long getNroPrecinto() {
        return nroPrecinto;
    }

    public void setNroPrecinto(Long nroPrecinto) {
        this.nroPrecinto = nroPrecinto;
    }

    public String getEstadoTrabajo() {
        return estadoTrabajo;
    }

    public void setEstadoTrabajo(String estadoTrabajo) {
        this.estadoTrabajo = estadoTrabajo;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaModificacionPrecinto() {
        return fechaModificacionPrecinto;
    }

    public void setFechaModificacionPrecinto(Date fechaModificacionPrecinto) {
        this.fechaModificacionPrecinto = fechaModificacionPrecinto;
    }

    public Long getNroPrecinto1() {
        return nroPrecinto1;
    }

    public void setNroPrecinto1(Long nroPrecinto1) {
        this.nroPrecinto1 = nroPrecinto1;
    }

    public String getTipoTrabajo() {
        return tipoTrabajo;
    }

    public void setTipoTrabajo(String tipoTrabajo) {
        this.tipoTrabajo = tipoTrabajo;
    }

    public Long getUsuarioModificacion_id() {
        return usuarioModificacion_id;
    }

    public void setUsuarioModificacion_id(Long usuarioModificacion_id) {
        this.usuarioModificacion_id = usuarioModificacion_id;
    }

    public Long getUsuarioModificacionPrecinto_id() {
        return usuarioModificacionPrecinto_id;
    }

    public void setUsuarioModificacionPrecinto_id(Long usuarioModificacionPrecinto_id) {
        this.usuarioModificacionPrecinto_id = usuarioModificacionPrecinto_id;
    }

    public String getUbicacionProvisoria() {
        return ubicacionProvisoria;
    }

    public void setUbicacionProvisoria(String ubicacionProvisoria) {
        this.ubicacionProvisoria = ubicacionProvisoria;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public Boolean getHabilitadoCerrar() {
        return habilitadoCerrar;
    }

    public void setHabilitadoCerrar(Boolean habilitadoCerrar) {
        this.habilitadoCerrar = habilitadoCerrar;
    }


}
