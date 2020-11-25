package com.aconcaguasf.basa.digitalize.dto.reporte;

/**
 * Created by acorrea on 23/10/2017.
 */
public class OperacionElementoReporte {

    private String tipoElemento;
    private String codigo;
    private String deposito;
    private String seccion;
    private String modulo;
    private String posicion;
    private Integer codigoBarras;
    private String origen;
    private String estado;
    private String rearchivoDe;
    private String codigoContenedor;
    private String textosYNumeros;
    private String codigoLoteReferencia;
    private String clasificacion;
    private String ubicacionProvisoria;

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getUbicacionProvisoria() {
        return ubicacionProvisoria;
    }

    public void setUbicacionProvisoria(String ubicacionProvisoria) {
        this.ubicacionProvisoria = ubicacionProvisoria;
    }

    public String getCodigoLoteReferencia() {
        return codigoLoteReferencia;
    }

    public void setCodigoLoteReferencia(String codigoLoteReferencia) {
        this.codigoLoteReferencia = codigoLoteReferencia;
    }

    public String getTextosYNumeros() {
        return textosYNumeros;
    }

    public void setTextosYNumeros(String textosYNumeros) {
        this.textosYNumeros = textosYNumeros;
    }

    public String getCodigoContenedor() {
        return codigoContenedor;
    }

    public void setCodigoContenedor(String codigoContenedor) {
        this.codigoContenedor = codigoContenedor;
    }

    public String getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(String tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Integer getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(Integer codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRearchivoDe() {
        return rearchivoDe;
    }

    public void setRearchivoDe(String rearchivoDe) {
        this.rearchivoDe = rearchivoDe;
    }
}
