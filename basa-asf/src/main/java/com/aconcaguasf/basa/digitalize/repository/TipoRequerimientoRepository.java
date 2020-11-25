package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.TipoRequerimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoRequerimientoRepository extends JpaRepository<TipoRequerimiento, Long> {

    @Query(value = "SELECT a FROM TipoRequerimiento a where a.clienteAsp = 1 ")
    public List<TipoRequerimiento> findTipoRequerimientoByClienteasp ();
}
