package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Lecturas;
import com.aconcaguasf.basa.digitalize.model.LecturasDetalles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LecturasRepository extends JpaRepository<Lecturas, Long> {

    @Query(value = "SELECT a FROM Lecturas a where a.Tipo = :tipo and a.estado_id = '0'" )
    List<Lecturas> findLectura(@Param("tipo") String tipo);

    @Query(value = "SELECT l FROM Lecturas l WHERE l.Tipo = :tipoLectura AND l.descripcion LIKE CONCAT('%',:numRemito,'-%') AND l.estado_id = 0")
    Lecturas findLecturaByRemito(@Param("numRemito") String numRemito, @Param("tipoLectura") String tipoLectura);

    @Query(value = "SELECT l FROM Lecturas l JOIN l.lecturasDetallesList ld WHERE " +
            "l.Tipo = 1 AND " +
            "ld.estado_id = '0' AND " +
            "ld.elemento_id = :elementoId")
    Lecturas findLecturaPlantaByElementoId(@Param("elementoId") Long elementoId);

}