package com.aconcaguasf.basa.digitalize.repository;


import com.aconcaguasf.basa.digitalize.model.RelacionReqConF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RelacionReqConRepository extends JpaRepository<RelacionReqConF, Long> {
    @Query(value = "SELECT a FROM RelacionReqConF a where a.requerimiento_id = :idReq ")
    List<RelacionReqConF> findByReqId(@Param("idReq") Long idReq);

    @Query("SELECT o FROM RelacionReqConF o " +
            "where o.requerimientoLite.depositoDefecto_id = :idDeposito " +
            "AND o.conceptoFacturable_id like :concepto_id " +
            "AND o.requerimientoLite.clienteEmp.personasJuridicas.id like :cliente " +
            "AND o.fecha >= :fechaDesde " +
            "AND o.fecha <= :fechaHasta ")
    Page<RelacionReqConF> findByFilters(@Param("idDeposito") Long idDeposito,
                                    @Param("concepto_id") String concepto_id,
                                    @Param("cliente") String cliente,
                                    //@Param("date") Date date,
                                    @Param("fechaDesde") Date fechaDesde,
                                    @Param("fechaHasta") Date fechaHasta,
                                    Pageable pageable);
}
