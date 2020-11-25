package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "requerimiento")
public class RequerimientoLite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Date fechaAlta;

    @Column(nullable = false)
    private Date fechaCierre;

    @Column(nullable = false)
    private Date fechaEntrega;

    @Column(nullable = false)
    private String horaAlta;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String prefijo;

    @Column(nullable = false)
    private String clienteAsp_id;

    @Column(nullable = false)
    private String clienteEmp_id;

    @Column(nullable = false)
    private Long direccionDefecto_id;

    @Column(nullable = false)
    private Long empleadoAutorizante_id;

    @Column(nullable = false)
    private Long empleadoSolicitante_id;

    @Column(nullable = false)
    private Long serie_id;

    @Column(nullable = false)
    private Long sucursal_id;

    @Column(nullable = false)
    private String tipoRequerimiento_id;

    @Column(nullable = false)
    private String usuario_id;

    @Column(nullable = false)
    private Long depositoDefecto_id;

    @Column(nullable = false)
    private String horaCierre;

    @Column(nullable = false)
    private String horaEntrega;

    @Column(nullable = false)
    private String listaPrecios_id;

    @Column(nullable = false)
    private String observaciones;

    @Column(nullable = false)
    private Long remito_id;

    @Column(nullable = false)
    private Long hojaRuta_id;

    @ManyToOne
    @JoinColumn(name="clienteEmp_id", insertable=false, updatable=false)
    private ClienteEmp clienteEmp;

    @ManyToOne
    @JoinColumn(name = "tipoRequerimiento_id", insertable = false, updatable = false)
    private TipoRequerimiento tipoRequerimiento;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "remito_id", insertable = false, updatable = false)
    private Remitos remito;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "hojaRuta_id", insertable = false, updatable = false)
    private HojaRuta hojaRuta;

    @Transient
    private LocalDate fechaEntregaString;

    public String getFechaEntregaString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(getFechaEntrega());
    }

    public void setFechaEntregaString(String fechaEntrega) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaEntregaString = LocalDate.parse(fechaEntrega, formatter);
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

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraAlta() {
        return horaAlta;
    }

    public void setHoraAlta(String horaAlta) {
        this.horaAlta = horaAlta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getClienteAsp_id() {
        return clienteAsp_id;
    }

    public void setClienteAsp_id(String clienteAsp_id) {
        this.clienteAsp_id = clienteAsp_id;
    }

    public String getClienteEmp_id() {
        return clienteEmp_id;
    }

    public ClienteEmp getClienteEmp() {
        return clienteEmp;
    }

    public void setClienteEmp(ClienteEmp clienteEmp) {
        this.clienteEmp = clienteEmp;
    }

    public TipoRequerimiento getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(TipoRequerimiento tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }

    public Remitos getRemito() {
        return remito;
    }

    public void setRemito(Remitos remito) {
        this.remito = remito;
    }

    public HojaRuta getHojaRuta() {
        return hojaRuta;
    }

    public void setHojaRuta(HojaRuta hojaRuta) {
        this.hojaRuta = hojaRuta;
    }

    public void setClienteEmp_id(String clienteEmp_id) {
        this.clienteEmp_id = clienteEmp_id;
    }

    public Long getDireccionDefecto_id() {
        return direccionDefecto_id;
    }

    public void setDireccionDefecto_id(Long direccionDefecto_id) {
        this.direccionDefecto_id = direccionDefecto_id;
    }

    public Long getEmpleadoAutorizante_id() {
        return empleadoAutorizante_id;
    }

    public void setEmpleadoAutorizante_id(Long empleadoAutorizante_id) {
        this.empleadoAutorizante_id = empleadoAutorizante_id;
    }

    public Long getEmpleadoSolicitante_id() {
        return empleadoSolicitante_id;
    }

    public void setEmpleadoSolicitante_id(Long empleadoSolicitante_id) {
        this.empleadoSolicitante_id = empleadoSolicitante_id;
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

    public String getTipoRequerimiento_id() {
        return tipoRequerimiento_id;
    }

    public void setTipoRequerimiento_id(String tipoRequerimiento_id) {
        this.tipoRequerimiento_id = tipoRequerimiento_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Long getDepositoDefecto_id() {
        return depositoDefecto_id;
    }

    public void setDepositoDefecto_id(Long depositoDefecto_id) {
        this.depositoDefecto_id = depositoDefecto_id;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getListaPrecios_id() {
        return listaPrecios_id;
    }

    public void setListaPrecios_id(String listaPrecios_id) {
        this.listaPrecios_id = listaPrecios_id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getRemito_id() {
        return remito_id;
    }

    public void setRemito_id(Long remito_id) {
        this.remito_id = remito_id;
    }

    public Long getHojaRuta_id() {
        return hojaRuta_id;
    }

    public void setHojaRuta_id(Long hojaRuta_id) {
        this.hojaRuta_id = hojaRuta_id;
    }
}
