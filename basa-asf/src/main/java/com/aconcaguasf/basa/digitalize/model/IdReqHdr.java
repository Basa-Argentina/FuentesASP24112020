package com.aconcaguasf.basa.digitalize.model;

import java.io.Serializable;

public class IdReqHdr implements Serializable {
    private Long idRequerimiento;
    private Long idHojaRuta;

    public IdReqHdr(Long idRequerimiento, Long idHojaRuta) {
        super();
        this.idRequerimiento = idRequerimiento;
        this.idHojaRuta = idHojaRuta;
    }
}
