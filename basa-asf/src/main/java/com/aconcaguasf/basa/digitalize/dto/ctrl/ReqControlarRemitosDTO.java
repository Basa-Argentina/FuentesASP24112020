package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.List;

public class ReqControlarRemitosDTO {
    private List<String> nroRemitosList;
    private String nroHdr;

    public ReqControlarRemitosDTO() {
    }

    public List<String> getNroRemitosList() {
        return nroRemitosList;
    }

    public void setNroRemitosList(List<String> nroRemitosList) {
        this.nroRemitosList = nroRemitosList;
    }

    public String getNroHdr() {
        return nroHdr;
    }

    public void setNroHdr(String nroHdr) {
        this.nroHdr = nroHdr;
    }
}
