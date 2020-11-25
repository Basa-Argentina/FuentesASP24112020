package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.List;

public class ReqControlarElementoDTO {

    private String nroHdr;
    private Boolean saltaControl;
    private List<String> elementosLeidos;

    public ReqControlarElementoDTO() {
    }

    public String getNroHdr() {
        return nroHdr;
    }

    public void setNroHdr(String nroHdr) {
        this.nroHdr = nroHdr;
    }

    public Boolean isSaltaControl() {
        return saltaControl;
    }

    public void setSaltaControl(Boolean saltaControl) {
        this.saltaControl = saltaControl;
    }

    public List<String> getElementosLeidos() {
        return elementosLeidos;
    }

    public void setElementosLeidos(List<String> elementosLeidos) {
        this.elementosLeidos = elementosLeidos;
    }
}
