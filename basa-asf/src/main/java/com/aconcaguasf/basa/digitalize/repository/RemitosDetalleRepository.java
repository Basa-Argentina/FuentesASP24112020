package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Remitos;
import com.aconcaguasf.basa.digitalize.model.RemitosDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RemitosDetalleRepository extends JpaRepository<RemitosDetalle, Long> {

    @Query(value = "SELECT * FROM remitosDetalle WHERE remito_id = :remitoId", nativeQuery = true)
    List<RemitosDetalle> findByRemitos(@Param("remitoId") Long remitoId);

    @Query(value = "SELECT rd.id FROM RemitosDetalle rd WHERE rd.remito_id = :remitoId AND rd.elemento_id = :elementoId")
    Long findRemitoDetalleIdByRemito_idAndElemento_id(@Param("remitoId") Long remitoId, @Param("elementoId") Long elementoId);
}
