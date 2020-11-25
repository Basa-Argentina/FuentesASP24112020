package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.HojaRuta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface HojaRutaRepository extends JpaRepository<HojaRuta, Long> {
    @Query(value = "SELECT distinct a.numero FROM HojaRuta a ")
    List<HojaRuta> findDistinctByNumero(Pageable pageable);

    @Query(value = "SELECT distinct a.numero FROM HojaRuta a ORDER BY a.numero DESC")
    List<String> findDistinctByNumeroOrderByNumeroDesc();

    @Query(value = "SELECT clasificacionDocumental.nombre " +
            "FROM clasificacionDocumental INNER JOIN " +
            "x_clasificacionDocumental_clienteEmpleados ON clasificacionDocumental.id = x_clasificacionDocumental_clienteEmpleados.clasificacionDocumental_id " +
            "WHERE x_clasificacionDocumental_clienteEmpleados.clienteEmpleados_id = :idUser " +
            "ORDER BY clasificacionDocumental.padre_id ASC", nativeQuery = true)
    List<String> getStates(@Param("idUser") Long idUser);

    @Modifying
    @Transactional
    @Query( value = "UPDATE dbo.hojaRuta SET estado = :estado " +
            "FROM(Select * from dbo.hojaRuta where numero = :numero ) e3  " +
            "WHERE hojaRuta.numero = e3.numero ", nativeQuery = true)
    void updateHdR(@Param("numero") Double numero,
                   @Param("estado") String estado);

    @Query(value = "SELECT a.id FROM HojaRuta a where a.numero = :numeroHdR ")
    Long findByNumero(@Param("numeroHdR") String numeroHdR);

    @Query(value = "SELECT TOP 1 a.* FROM HojaRuta a where a.numero = :numeroHdR", nativeQuery = true)
    HojaRuta findHdrByNumero(@Param("numeroHdR") String numeroHdR);

    @Query(value = "select distinct top 1  h.numero from requerimiento r\n" +
            "inner join operacion o                     on r.id             = o.requerimiento_id        \n" +
            "inner join x_operacion_elemento e          on o.id             = e.operacion_id            \n" +
            "inner join hojaRuta_operacionElemento h_op on e.id             = h_op.operacionElemento_id \n" +
            "inner join hojaRuta h                      on h_op.hojaRuta_id = h.id                      \n" +
            "where r.id = :idRequerimiento", nativeQuery = true)
    String findHdrByIdRequerimiento(@Param("idRequerimiento") Long idRequerimiento);


    @Query(value = "select distinct CAST(r.numero AS VARCHAR(60)) from requerimiento r\n" +
            "inner join operacion o                     on r.id             = o.requerimiento_id        \n" +
            "inner join x_operacion_elemento e          on o.id             = e.operacion_id            \n" +
            "inner join hojaRuta_operacionElemento h_op on e.id             = h_op.operacionElemento_id \n" +
            "inner join hojaRuta h                      on h_op.hojaRuta_id = h.id                      \n" +
            "where h.numero = :numHdr", nativeQuery = true)
    List<String> findIdRequerimientoByHdr(@Param("numHdr") String numHdr);


    @Query(value = "SELECT MAX(hr.numero) FROM HojaRuta hr", nativeQuery = true)
    String findLastHdr();

    HojaRuta findFirstById(Long idHdr);

    @Query(value = "SELECT hdr.numero FROM Requerimiento r " +
            "INNER JOIN Req_hoja_ruta rhdr ON r.id = rhdr.id_requerimiento " +
            "INNER JOIN Hojaruta hdr ON rhdr.id_hoja_ruta= hdr.id " +
            "WHERE rhdr.id_estado <> 2 AND r.numero = :numReq", nativeQuery = true)
    String findNroHdrByIdReq(@Param("numReq") String numReq);
}
