package com.aconcaguasf.basa.digitalize.dto.ctrl;

public class ResponseDTO {

    private String estado;
    private Object respuesta;
    private String codigo;
    private String datoAdicional;

    public ResponseDTO() {
    }

    public ResponseDTO(String respuesta) {
        this.respuesta = respuesta;
    }

    public ResponseDTO(String codigo, Object respuesta) {
        this.codigo    = codigo;
        this.respuesta = respuesta;
    }

    public ResponseDTO(String codigo, String respuesta, String datoAdicional) {
        this.respuesta = respuesta;
        this.codigo = codigo;
        this.datoAdicional = datoAdicional;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Object getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDatoAdicional() {
        return datoAdicional;
    }

    public void setDatoAdicional(String datoAdicional) {
        this.datoAdicional = datoAdicional;
    }
}
