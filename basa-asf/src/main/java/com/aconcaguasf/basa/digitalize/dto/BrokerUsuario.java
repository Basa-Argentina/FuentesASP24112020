package com.aconcaguasf.basa.digitalize.dto;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.UsersxGrupo;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by acorrea on 10/07/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerUsuario {

UsersxGrupo user;

Operaciones operacion;

    public UsersxGrupo getUser() {
        return user;
    }

    public void setUser(UsersxGrupo user) {
        this.user = user;
    }

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }
}