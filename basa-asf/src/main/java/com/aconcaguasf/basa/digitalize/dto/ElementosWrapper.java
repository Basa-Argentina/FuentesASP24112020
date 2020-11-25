package com.aconcaguasf.basa.digitalize.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ElementosWrapper {

    List<Long> elemento_id;

    public List<Long> getElemento_id() {
        return elemento_id;
    }

    public void setElemento_id(List<Long> elemento_id) {
        this.elemento_id = elemento_id;
    }
}