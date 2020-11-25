package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Operaciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OperacionesRepository extends JpaRepository<Operaciones, Long> {

    @Query("SELECT  o FROM Operaciones o where o.deposito_id = :idDeposito " +
            "AND o.requerimiento.tipoRequerimiento_id like :tipoRequerimiento " +
            "AND o.tipoOperaciones.descripcion like :estado " +
            "AND o.clienteEmp.personasJuridicas.id like :cliente " +
            "AND o.fechaEntrega > :date " )
    Page<Operaciones> findByFilters(@Param("idDeposito") Long idDeposito,
                                                                                           @Param("tipoRequerimiento") String tipoRequerimiento,
                                                                                           @Param("estado") String estado,
                                                                                           @Param("cliente") String cliente,
                                                                                           @Param("date") Date date,
                                                                                           Pageable pageable);

    /* Buscar todos los requerimientos sin distincion de Sucursal */
    @Query(value = "SELECT   o FROM Operaciones o where o.deposito_id = 4 " +
            "AND o.requerimiento.tipoRequerimiento_id like :tipoRequerimiento " +
            "AND o.tipoOperaciones.descripcion like :estado " +
            "AND o.clienteEmp.personasJuridicas.id like :cliente " +
            "AND o.fechaEntrega > :date ")
    Page<Operaciones> findByAllBranchOfficeFilters(@Param("tipoRequerimiento") String tipoRequerimiento,
                                                   @Param("estado") String estado,
                                                   @Param("cliente") String cliente,
                                                   @Param("date") Date date,
                                                   Pageable pageable);

    @Query(value = "SELECT DISTINCT o " +
            "FROM Operaciones o INNER JOIN o.relacionOpEl rel " +
            "where rel.id IN :rel")
    List<Operaciones> findByRelOpId(@Param("rel") List<Long> rel);

    @Query(value = "SELECT o FROM Operaciones o where o.tipoOperacion_id in (17,58) ")
    List<Operaciones> findControlarRemitos();

    @Query(value = "SELECT a.id FROM Operaciones a where a.requerimiento.hojaRuta.numero = :numeroHdR ")
    List<Long> findByHdR(@Param("numeroHdR") String numeroHdR);

    Operaciones findFirstByRequerimiento_id(Long idReq);

    @Query(value = "SELECT o FROM Operaciones o WHERE o.requerimiento.numero = :numReq")
    Page<Operaciones> findOperacionesByNumReq(@Param("numReq") String numReq, Pageable pageable);

    @Query(value = "SELECT o FROM Operaciones o JOIN o.relacionOpEl r WHERE r.codigoElemento.codigo = :codigoElemento")
    Page<Operaciones> findOperacionesByCodigoElemento(@Param("codigoElemento") String codigoElemento, Pageable pageable);

    //@QueryHints(@QueryHint(name = "JDBC_MAX_ROWS", value = "1"))
    //@QueryHints(value = { @QueryHint(name = "JDBC_MAX_ROWS", value = "1")}, forCounting = false)
    //@Query(value = "SELECT o FROM Operaciones o " +"JOIN o.relacionOpEl e " +"JOIN o.requerimiento r " +"JOIN o.requerimiento.tipoRequerimiento tr " +"WHERE e.elemento_id = :idElemento " +"AND tr.tipoMovimiento = :tipoMovimiento " +"ORDER BY o.id DESC")

    @Query(value = "SELECT TOP 1 o.* FROM operacion o" +
            "  INNER JOIN x_operacion_elemento x on o.id                   = x.operacion_id" +
            "  INNER JOIN requerimiento r        on o.requerimiento_id     = r.id" +
            "  INNER JOIN tipos_requerimiento t  on r.tipoRequerimiento_id = t.id" +
            "    WHERE x.elemento_id    = :idElemento" +
            "      AND t.tipoMovimiento = :tipoMovimiento" +
            "  ORDER BY o.id DESC", nativeQuery = true)
    Operaciones findFirstByRelacionOpElAndRequerimientoTipoRequerimientoTipoMovimiento(@Param("idElemento") String idElemento, @Param("tipoMovimiento") String tipoMovimiento);

    @Query("select o FROM Operaciones o WHERE o.requerimiento.remito.numeroSinPrefijo LIKE CONCAT('%',:numero,'%')")
    Operaciones findByNumRemito(@Param("numero") String numero);

}
