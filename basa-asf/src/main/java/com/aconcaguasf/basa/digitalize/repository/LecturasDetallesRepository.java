package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.LecturasDetalles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LecturasDetallesRepository extends JpaRepository<LecturasDetalles, Long> {

    @Query(value = "SELECT d.* FROM lecturas l " +
            "INNER JOIN lecturaDetalle d ON l.id = d.lectura_id " +
            "WHERE l.tipo = :tipoLectura " +
            "AND d.elemento_id = :idElemento " +
            "AND d.estado_id = :idEstado", nativeQuery = true)
    LecturasDetalles findByElemento_idAndEstado_id(@Param("tipoLectura") String tipoLectura, @Param("idElemento") Long idElemento, @Param("idEstado") String idEstado);

    @Query("SELECT d FROM LecturasDetalles d WHERE d.id IN :idList")
    List<LecturasDetalles> findByIdIn(@Param("idList") List<Long> idList);

}
