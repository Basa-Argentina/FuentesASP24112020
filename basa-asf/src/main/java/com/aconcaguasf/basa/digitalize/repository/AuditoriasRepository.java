package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Auditoria;
import com.aconcaguasf.basa.digitalize.model.Personas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriasRepository extends JpaRepository<Auditoria, Long> {
}
