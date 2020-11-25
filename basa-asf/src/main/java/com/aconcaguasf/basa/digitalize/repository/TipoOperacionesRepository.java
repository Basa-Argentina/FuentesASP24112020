package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.TipoOperaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoOperacionesRepository extends JpaRepository<TipoOperaciones, Long> {
    @Query(value = "SELECT distinct a.descripcion FROM TipoOperaciones a ORDER BY a.descripcion ASC")
    List<TipoOperaciones> findDistinctByTipoOperacion ();

    @Query(value = "SELECT a.id FROM TipoOperaciones a where a.tipoRequerimiento_id = :idReq")
    String findIdByTipoReq (@Param("idReq")Long idReq);

    TipoOperaciones findById(Long idTipoOperacion);

    @Query(value = "SELECT t_op_anterior.*" +
                    " FROM tipos_operacion t_op_actual" +
                    " INNER JOIN tipos_operacion t_op_anterior ON t_op_actual.id = t_op_anterior.tipoOperacionSiguiente_id" +
                    "   WHERE LOWER(t_op_anterior.descripcion) LIKE '%asignar_hdr%'" +
                    "     AND t_op_actual.id = :idTipoOperacionActual", nativeQuery = true)
    TipoOperaciones findByPreviousIdAsgHdR(@Param("idTipoOperacionActual") Long idTipoOperacionActual);

    @Query(value = "SELECT t_op_anterior.*" +
            " FROM tipos_operacion t_op_actual" +
            " INNER JOIN tipos_operacion t_op_anterior ON t_op_actual.id = t_op_anterior.tipoOperacionSiguiente_id" +
            "   WHERE LOWER(t_op_anterior.descripcion) LIKE '%controlar_elemento%'" +
            "     AND t_op_actual.id = :idTipoOperacionActual", nativeQuery = true)
    TipoOperaciones findByPreviousIdCtrlElem(@Param("idTipoOperacionActual") Long idTipoOperacionActual);

}
