package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "operacion")
public class Operaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Date fechaAlta;

    @Column(nullable = false)
    private Date fechaCierre;

    @Column(nullable = false)
    private String horaAlta;

    @Column(nullable = false)
    private String horaCierre;

    @Column(nullable = false)
    private Long clienteAsp_id;

    @Column(nullable = false)
    private Long clienteEmp_id;

    @Column(nullable = false)
    private Long deposito_id;

    @Column(nullable = false)
    private Long requerimiento_id;

    @Column(nullable = false)
    private String tipoOperacion_id;

    @Column(nullable = false)
    private Long usuario_id;

    @Column(nullable = false)
    private Date fechaEntrega;

    @Column(nullable = false)
    private String horaEntrega;

    @Column(nullable = false)
    private Integer cantidadOmitidos;

    @Column(nullable = false)
    private Integer cantidadPendientes;

    @Column(nullable = false)
    private Integer cantidadProcesados;

    @Column(nullable = false)
    private Integer cantidadProcesadosParaTraspaso;

    @Column(nullable = false)
    private Boolean finalizarError;

    @Column(nullable = false)
    private Boolean finalizarOk;

    @Column(nullable = false)
    private Boolean traspasar;

    @Column(nullable = false)
    private String observaciones;

    @Column(nullable = false)
    private Integer cantidadImpresiones;

    @Column(nullable = false)
    private Long usuarioAsignado_id;

    //Property aggregation without mapping
    @Transient
    private String turno;

    @Transient
    private LocalDate fechaEntregaString;


    @ManyToOne
    @JoinColumn(name="clienteEmp_id", insertable=false, updatable=false)
    private ClienteEmp clienteEmp;

    @ManyToOne
    @JoinColumn(name="clienteAsp_id", insertable=false, updatable=false)
    private ClientesAsp clientesAsp;

    @ManyToOne
    @JoinColumn(name="requerimiento_id", insertable=false, updatable=false)
    private Requerimiento requerimiento;

    @OneToMany(mappedBy="operacion_id")
    @OrderBy("elemento_id")
    private Set<RelacionOpEl> relacionOpEl;

    @ManyToOne
    @JoinColumn(name="deposito_id", insertable=false, updatable=false)
    private Depositos depositos;


    @ManyToOne
    @JoinColumn(name="tipoOperacion_id", insertable=false, updatable=false)
    private TipoOperaciones tipoOperaciones;


    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="usuarioAsignado_id", insertable=false, updatable=false)
    private Nombres usrAsignado;

    private Long usuarioFinalizo_id;
    private String origen_finalizado;

    public Nombres getUsrAsignado() {
        return usrAsignado;
    }

    public void setUsrAsignado(Nombres usrAsignado) {
        this.usrAsignado = usrAsignado;
    }

    public String getTurno() {
        String horaEntrega = getHoraEntrega();
        int hora = Integer.parseInt(horaEntrega.substring(0, horaEntrega.indexOf(":")));
        turno = hora<=12?"Mañana":"Tarde";
        return turno;
    }
    public Set<RelacionOpEl> getRelacionOpEl() {
        return relacionOpEl;
    }

    public void setRelacionOpEl(Set<RelacionOpEl> relacionOpEl) {
        this.relacionOpEl = relacionOpEl;
    }

    public void addRelacionOpEl(RelacionOpEl relacionOpEl){
        this.relacionOpEl.add(relacionOpEl);
    }

    public Requerimiento getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(Requerimiento requerimiento) {
        this.requerimiento = requerimiento;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public TipoOperaciones getTipoOperaciones() {
        return tipoOperaciones;
    }

    public void setTipoOperaciones(TipoOperaciones tipoOperaciones) {
        this.tipoOperaciones = tipoOperaciones;
    }

    public void setCantidadImpresiones(Integer cantidadImpresiones) {
        this.cantidadImpresiones = cantidadImpresiones;
    }

    public Long getUsuarioAsignado_id() {
        return usuarioAsignado_id;
    }

    public void setUsuarioAsignado_id(Long usuarioAsignado_id) {
        this.usuarioAsignado_id = usuarioAsignado_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getHoraAlta() {
        return horaAlta;
    }

    public void setHoraAlta(String horaAlta) {
        this.horaAlta = horaAlta;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
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

    public Long getDeposito_id() {
        return deposito_id;
    }

    public void setDeposito_id(Long deposito_id) {
        this.deposito_id = deposito_id;
    }

    public Long getRequerimiento_id() {
        return requerimiento_id;
    }

    public void setRequerimiento_id(Long requerimiento_id) {
        this.requerimiento_id = requerimiento_id;
    }

    public String getTipoOperacion_id() {
        return tipoOperacion_id;
    }

    public void setTipoOperacion_id(String tipoOperacion_id) {
        this.tipoOperacion_id = tipoOperacion_id;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Date getFechaEntrega() { return fechaEntrega; }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public Integer getCantidadOmitidos() {
        return cantidadOmitidos;
    }

    public void setCantidadOmitidos(Integer cantidadOmitidos) {
        this.cantidadOmitidos = cantidadOmitidos;
    }

    public Integer getCantidadPendientes() {
        return cantidadPendientes;
    }

    public void setCantidadPendientes(Integer cantidadPendientes) {
        this.cantidadPendientes = cantidadPendientes;
    }

    public Integer getCantidadProcesados() {
        return cantidadProcesados;
    }

    public void setCantidadProcesados(Integer cantidadProcesados) {
        this.cantidadProcesados = cantidadProcesados;
    }

    public Integer getCantidadProcesadosParaTraspaso() {
        return cantidadProcesadosParaTraspaso;
    }

    public void setCantidadProcesadosParaTraspaso(Integer cantidadProcesadosParaTraspaso) {
        this.cantidadProcesadosParaTraspaso = cantidadProcesadosParaTraspaso;
    }

    public Boolean getFinalizarError() {
        return finalizarError;
    }

    public void setFinalizarError(Boolean finalizarError) {
        this.finalizarError = finalizarError;
    }

    public Boolean getFinalizarOk() {
        return finalizarOk;
    }

    public void setFinalizarOk(Boolean finalizarOk) {
        this.finalizarOk = finalizarOk;
    }

    public Boolean getTraspasar() {
        return traspasar;
    }

    public void setTraspasar(Boolean traspasar) {
        this.traspasar = traspasar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCantidadImpresiones() {
        return cantidadImpresiones;
    }


    public ClienteEmp getClienteEmp() {
        return clienteEmp;
    }

    public void setClienteEmp(ClienteEmp clienteEmp) {
        this.clienteEmp = clienteEmp;
    }

    public ClientesAsp getClientesAsp() {
        return clientesAsp;
    }

    public void setClientesAsp(ClientesAsp clientesAsp) {
        this.clientesAsp = clientesAsp;
    }


    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public String getFechaEntregaString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(getFechaEntrega());
    }
    public String getFechaStringEntrega() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (fechaEntregaString!= null)
        return this.fechaEntregaString.format(formatter);
        else return null;
    }

    public void setFechaEntregaString(String fechaEntrega) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaEntregaString = LocalDate.parse(fechaEntrega, formatter);
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Long getUsuarioFinalizo_id() {
        return usuarioFinalizo_id;
    }

    public void setUsuarioFinalizo_id(Long usuarioFinalizo_id) {
        this.usuarioFinalizo_id = usuarioFinalizo_id;
    }

    public String getOrigen_finalizado() {
        return origen_finalizado;
    }

    public void setOrigen_finalizado(String origen_finalizado) {
        this.origen_finalizado = origen_finalizado;
    }
}
