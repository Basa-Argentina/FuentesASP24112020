package com.aconcaguasf.basa.digitalize.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "configuracion_reporte")
public class ConfigReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_jrxml")
    private String fileJrxml;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "report_name")
    private String reportName;

    @OneToMany(mappedBy = "configReportId")
    List<ConfigReportParam> params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileJrxml() {
        return fileJrxml;
    }

    public void setFileJrxml(String fileJrxml) {
        this.fileJrxml = fileJrxml;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<ConfigReportParam> getParams() {
        return params;
    }

    public void setParams(List<ConfigReportParam> params) {
        this.params = params;
    }
}
