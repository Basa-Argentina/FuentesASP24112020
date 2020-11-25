package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.RequerimientoHojaRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReqHojaRutaRepository extends JpaRepository<RequerimientoHojaRuta, Long> {
    RequerimientoHojaRuta findFirstByIdRequerimientoOrderByFechaCreacionDesc(Long idReq);
    List<RequerimientoHojaRuta> findAllByIdHojaRuta(Long idHdr);
    List<RequerimientoHojaRuta> findByIdRequerimiento(Long idReq);

    @Query(value = "SELECT r.* FROM req_hoja_ruta r " +
            "WHERE r.id_estado != :idEstado " +
            "AND r.id_requerimiento = :idReq " +
            "AND r.id_hoja_ruta != :idHojaRuta", nativeQuery = true)
    List<RequerimientoHojaRuta> getRequerimientoHojaRutaPendienteControlado(@Param("idHojaRuta") Long idHojaRuta, @Param("idReq") Long idReq, @Param("idEstado") Long idEstado);

}
