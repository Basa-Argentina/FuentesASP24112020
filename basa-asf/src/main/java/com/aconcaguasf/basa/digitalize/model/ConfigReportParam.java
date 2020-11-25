package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;

@Entity
@Table(name = "config_reporte_parametros")
public class ConfigReportParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_type")
    private String paramType;

    @Column(name = "configuracion_reporte_id")
    private Long configReportId;

    @Column(name = "param_order")
    private String paramOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public Long getConfigReportId() {
        return configReportId;
    }

    public void setConfigReportId(Long configReportId) {
        this.configReportId = configReportId;
    }

    public String getParamOrder() {
        return paramOrder;
    }

    public void setParamOrder(String paramOrder) {
        this.paramOrder = paramOrder;
    }
}
