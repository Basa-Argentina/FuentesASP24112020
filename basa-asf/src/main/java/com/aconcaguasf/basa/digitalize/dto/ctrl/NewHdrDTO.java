package com.aconcaguasf.basa.digitalize.dto.ctrl;

import java.util.List;

public class NewHdrDTO {
    private String numHdr;
    private List<String> numReqList;

    public NewHdrDTO() {
    }

    public String getNumHdr() {
        return numHdr;
    }

    public void setNumHdr(String numHdr) {
        this.numHdr = numHdr;
    }

    public List<String> getNumReqList() {
        return numReqList;
    }

    public void setNumReqList(List<String> numReqList) {
        this.numReqList = numReqList;
    }
}
