package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.ConfigReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConfigReportRepository extends JpaRepository<ConfigReport, Long> {
    ConfigReport findByReportName(String reportName);
    
}
