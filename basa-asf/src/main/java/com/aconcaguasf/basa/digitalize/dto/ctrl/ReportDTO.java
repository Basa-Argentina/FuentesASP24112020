package com.aconcaguasf.basa.digitalize.dto.ctrl;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ReportDTO {
    private String reportName;
    private Map<String, Object> params;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
