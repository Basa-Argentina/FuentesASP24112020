package com.aconcaguasf.basa.digitalize.exceptions;

import com.aconcaguasf.basa.digitalize.config.Const;

import javax.persistence.NonUniqueResultException;

public class CustomException {
    private static CustomException instance;

    private String message;
    private String code;
    private String origen;

    public static CustomException getInstance(){
        instance = (instance != null) ? instance : new CustomException();
        return instance;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getOrigen() {
        return origen;
    }

    public void setCustomException(Exception pEx, String pOrigen) {

        if (pEx.getCause() instanceof NonUniqueResultException) {

            this.code    = Const.DB_100;
            this.message = Const.DB_100_MENSAJE.concat(" en ".concat(pOrigen));
            this.origen  = pOrigen;

        } else {

            this.code    = Const.ERR;
            this.message = pEx.getMessage();

        }
    }
}
