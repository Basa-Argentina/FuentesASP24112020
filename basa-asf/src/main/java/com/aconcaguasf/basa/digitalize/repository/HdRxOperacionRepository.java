package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HdRxOperacionRepository extends JpaRepository<HdRxOperacion, Long> {

    @Query(value = "SELECT o.operacionElemento_id " +
            "FROM HdRxOperacion o INNER JOIN o.hojaRuta hdr " +
            "where hdr.numero like :numero ")
    List<String> findByNumero(@Param("numero") String numero);

}
