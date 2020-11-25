package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Remitos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RemitosRepository extends JpaRepository<Remitos, Long> {

    @Query(value = "SELECT a.elemento_id FROM RemitosDetalle a,Elementos e " +
            "inner join a.remitos where  a.remitos.numRequerimiento like :numRequerimiento and e.id=a.elemento_id  ")
    List<Long> findElementosByReq(@Param("numRequerimiento") String numRequerimiento);

    @Query(value = "SELECT * from remitos rem WHERE rem.numeroSinPrefijo LIKE CONCAT('%',:numero,'%') AND EXISTS (SELECT 1 FROM requerimiento req WHERE req.remito_id = rem.id )", nativeQuery = true)
    Remitos findByNumeroSinPrefijo(@Param("numero") String numero);

}
