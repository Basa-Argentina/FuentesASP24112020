package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Requerimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RequerimientoRepository extends JpaRepository<Requerimiento, Long> {

    @Modifying
    @Transactional
    @Query( value = "UPDATE dbo.requerimiento SET hojaRuta_id = :IdHojaRuta " +
            "FROM(Select * from dbo.requerimiento where id = :idReq ) e3  " +
            "WHERE requerimiento.id = e3.id ", nativeQuery = true)
    void updateHdR_id(@Param("IdHojaRuta") Long IdHojaRuta,
                   @Param("idReq") Long idReq);

    @Query(value = "SELECT o.estado " +
            "FROM requerimiento r " +
            "INNER JOIN Operacion o ON r.id = o.requerimiento_id " +
            "WHERE r.numero = :numero", nativeQuery = true)
    String findEstadoOpByNroReq(@Param("numero") Long numero);

    @Query(value = "SELECT r.id " +
            "FROM requerimiento r " +
            "WHERE r.numero = :numero", nativeQuery = true)
    Long findIdReqNroReq(@Param("numero") Long numero);

    Requerimiento findRequerimientoByNumero(String numero);

    Requerimiento findRequerimientosById(Long idReq) ;

    List<Requerimiento> findByHojaRuta_id(Long idHdr) ;

    Requerimiento findByRemito_id(Long idRemito) ;

}
