package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemitoProcesoDTO {

    private Long                 numeroRemito;
    private Long                 numeroRequerimiento;
    private Long                 tipoRequerimiento;
    private String               descTipoRequerimiento;
    private Long                 cantidadElementos;
    private List<String>         elementosSobrantesCamion;
    private List<String>         elementosFaltantesCamion;
    private List<String>         elementosFaltantesPlanta;
    private Long                 fechaEntregaRemito;
    private Date                 fechaEntregaRequerimiento;
    private String               turno;
    private String               descCliente;
    private String               procesado;
    private List<Long>           idLecturasPlanta;
    private List<Long>           idLecturasCamion;
    private List<ElementoRemito> elementosAgrupados;
    private String               metodo;


    public Long getNumeroRemito() {
        return numeroRemito;
    }

    public void setNumeroRemito(Long numeroRemito) {
        this.numeroRemito = numeroRemito;
    }

    public Long getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(Long cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

    public List<String> getElementosSobrantesCamion() {
        return elementosSobrantesCamion;
    }

    public void setElementosSobrantesCamion(List<String> elementosSobrantesCamion) {
        this.elementosSobrantesCamion = elementosSobrantesCamion;
    }

    public List<String> getElementosFaltantesCamion() {
        return elementosFaltantesCamion;
    }

    public void setElementosFaltantesCamion(List<String> elementosFaltantesCamion) {
        this.elementosFaltantesCamion = elementosFaltantesCamion;
    }

    public List<String> getElementosFaltantesPlanta() {
        return elementosFaltantesPlanta;
    }

    public void setElementosFaltantesPlanta(List<String> elementosFaltantesPlanta) {
        this.elementosFaltantesPlanta = elementosFaltantesPlanta;
    }

    public long getFechaEntregaRemito() {
        return fechaEntregaRemito;
    }

    public void setFechaEntregaRemito(long fechaEntregaRemito) {
        this.fechaEntregaRemito = fechaEntregaRemito;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<Long> getIdLecturasPlanta() {
        return idLecturasPlanta;
    }

    public void setIdLecturasPlanta(List<Long> idLecturasPlanta) {
        this.idLecturasPlanta = idLecturasPlanta;
    }

    public void addIdLecturasPlanta(Long idLecturaDetalle){
        if (this.idLecturasPlanta == null)
            this.idLecturasPlanta = new ArrayList<>();
        this.idLecturasPlanta.add(idLecturaDetalle);
    }

    public List<Long> getIdLecturasCamion() {
        return idLecturasCamion;
    }

    public void setIdLecturasCamion(List<Long> idLecturasCamion) {
        this.idLecturasCamion = idLecturasCamion;
    }

    public void addIdLecturasCamion(Long idLecturaDetalle){
        if (this.idLecturasCamion == null)
            this.idLecturasCamion = new ArrayList<>();
        this.idLecturasCamion.add(idLecturaDetalle);
    }

    public List<ElementoRemito> getElementosAgrupados() {
        return elementosAgrupados;
    }

    public void setElementosAgrupados(List<ElementoRemito> elementosAgrupados) {
        this.elementosAgrupados = elementosAgrupados;
    }

    public void addElementoAgrupado(ElementoRemito elementoRemito){
        if (this.elementosAgrupados == null)
            this.elementosAgrupados = new ArrayList<>();
        this.elementosAgrupados.add(elementoRemito);
    }

    public Long getNumeroRequerimiento() {
        return numeroRequerimiento;
    }

    public void setNumeroRequerimiento(Long numeroRequerimiento) {
        this.numeroRequerimiento = numeroRequerimiento;
    }

    public String getDescTipoRequerimiento() {
        return descTipoRequerimiento;
    }

    public void setDescTipoRequerimiento(String descTipoRequerimiento) {
        this.descTipoRequerimiento = descTipoRequerimiento;
    }

    public Date getFechaEntregaRequerimiento() {
        return fechaEntregaRequerimiento;
    }

    public void setFechaEntregaRequerimiento(Date fechaEntregaRequerimiento) {
        this.fechaEntregaRequerimiento = fechaEntregaRequerimiento;
    }

    public String getDescCliente() {
        return descCliente;
    }

    public void setDescCliente(String descCliente) {
        this.descCliente = descCliente;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public static class ElementoRemito extends RemitoProcesoDTO{
        Long idElemento;
        String codigoElemento;
        String codigoGrupo;
        String grupo;
        Long codigoCarga;

        public ElementoRemito(Long idElemento, String codigoElemento, String codigoGrupo, String grupo, Long codigoCarga) {
            this.idElemento     = idElemento;
            this.codigoElemento = codigoElemento;
            this.codigoGrupo    = codigoGrupo;
            this.grupo          = grupo;
            this.codigoCarga    = codigoCarga;
        }

        public Long getIdElemento() {
            return idElemento;
        }

        public void setIdElemento(Long idElemento) {
            this.idElemento = idElemento;
        }

        public String getCodigoElemento() {
            return codigoElemento;
        }

        public void setCodigoElemento(String codigoElemento) {
            this.codigoElemento = codigoElemento;
        }

        public String getCodigoGrupo() {
            return codigoGrupo;
        }

        public void setCodigoGrupo(String codigoGrupo) {
            this.codigoGrupo = codigoGrupo;
        }

        public String getGrupo() {
            return grupo;
        }

        public void setGrupo(String grupo) {
            this.grupo = grupo;
        }

        public Long getCodigoCarga() {
            return codigoCarga;
        }

        public void setCodigoCarga(Long codigoCarga) {
            this.codigoCarga = codigoCarga;
        }
    }

    public Long getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    public void setTipoRequerimiento(Long tipoRequerimiento) {
        this.tipoRequerimiento = tipoRequerimiento;
    }
}
