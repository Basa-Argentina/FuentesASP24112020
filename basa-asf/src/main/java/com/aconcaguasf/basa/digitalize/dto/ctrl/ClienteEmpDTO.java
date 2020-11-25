package com.aconcaguasf.basa.digitalize.dto.ctrl;

public class ClienteEmpDTO {
    private String personaJuridicaId;
    private String personaJuridicaRazonSocial;
    private Long codigo;

    public ClienteEmpDTO(String personaJuridicaId, String personaJuridicaRazonSocial, Long codigo) {
        this.personaJuridicaId = personaJuridicaId;
        this.personaJuridicaRazonSocial = personaJuridicaRazonSocial;
        this.codigo = codigo;
    }

    public String getPersonaJuridicaId() {
        return personaJuridicaId;
    }

    public void setPersonaJuridicaId(String personaJuridicaId) {
        this.personaJuridicaId = personaJuridicaId;
    }

    public String getPersonaJuridicaRazonSocial() {
        return personaJuridicaRazonSocial;
    }

    public void setPersonaJuridicaRazonSocial(String personaJuridicaRazonSocial) {
        this.personaJuridicaRazonSocial = personaJuridicaRazonSocial;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
}
