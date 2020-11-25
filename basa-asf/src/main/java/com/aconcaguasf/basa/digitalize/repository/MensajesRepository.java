package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Mensajes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajesRepository extends JpaRepository<Mensajes, Long> {
    @Query( "select m.requerimiento_id from Mensajes m " +
            "where m.usrDestino_id = :idUser and m.leido=false")
    List<Long> findByCurrentUsr(@Param("idUser")Long idUser);

    @Query( "select m from Mensajes m " +
            "where m.requerimiento_id = :idReq " +
            "order by m.fechaCreacion asc")
    List<Mensajes> findByRequerimientoId(@Param("idReq")Long idReq);
}
