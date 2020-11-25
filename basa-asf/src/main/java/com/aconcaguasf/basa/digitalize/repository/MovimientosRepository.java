package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {

    @Query("SELECT m FROM Movimientos m WHERE m.remito_id = :idRemito")
    List<Movimientos> findByRemito_id(@Param("idRemito") Long idRemito);

    @Query("SELECT MAX(m.codigoCarga) FROM Movimientos m")
    Long findMaxCodigoCarga();

    @Query("SELECT m FROM Movimientos m WHERE m.remito_id = :idRemito AND m.elemento_id = :idElemento")
    Movimientos findByRemito_idAndElemento_id(@Param("idRemito") Long idRemito, @Param("idElemento") Long idElemento);

}
