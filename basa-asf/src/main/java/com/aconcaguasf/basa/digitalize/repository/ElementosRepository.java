package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.Elementos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ElementosRepository extends JpaRepository<Elementos, Long> {

    @Query( "SELECT o FROM Elementos o WHERE id IN :ids" )
    List<Elementos> findByElementosIds(@Param("ids") List<Long> idElemento);

    @Modifying
    @Transactional
    @Query( value = "UPDATE" +
            "    dbo.elementos " +
            "SET " +
            "estado = :idReq " +
            "FROM" +
            "   (SELECT TOP (:cantidad) [id] FROM elementos " +
            "WHERE estado = 'Creado' AND tipoElemento_id = :idTipo " +
            "AND clienteAsp_id = :clienteAsp " +
            "AND clienteEmp_id IS NULL " +
            "AND usuarioAsignacionElemento_id = :user_id " +
            "ORDER BY id asc) e3 " +
            "WHERE elementos.id = e3.id ", nativeQuery = true)
    void updateReserva(@Param("cantidad") Integer cantidad,
                       @Param("idReq") String idReq,
                       @Param("idTipo") Integer idTipo,
                       @Param("clienteAsp") Long clienteAsp,
                       @Param("user_id") Long user_id);

    @Query(value = "SELECT count(1) cantidad FROM elementos e " +
                    "WHERE estado = 'Creado' " +
                    "  AND tipoElemento_id = :idTipo " +
                    "  AND clienteAsp_id = :clienteAsp " +
                    "  AND clienteEmp_id IS NULL " +
                    "  AND usuarioAsignacionElemento_id = :user_id", nativeQuery = true)
    Integer getCantidadDisponible(@Param("idTipo") Integer idTipo,
                                  @Param("clienteAsp") Long clienteAsp,
                                  @Param("user_id") Long user_id);

    @Query( "SELECT o.id FROM Elementos o WHERE o.codigo = :codigo AND o.clienteAsp_id = :clienteAsp" )
    String findByCodigo(@Param("codigo") String codigo,
                        @Param("clienteAsp") Long clienteAsp);

    /* Search Element by Code and Client */
    @Query( "SELECT e.id " +
            "FROM Elementos e " +
            "WHERE e.codigo = :codigo AND e.clienteAsp_id = :clienteAsp AND e.clienteEmp_id = :clienteEmp" )
    String findByCodigoAndCliente(@Param("codigo") String codigo,
                                  @Param("clienteAsp") Long clienteAsp,
                                  @Param("clienteEmp") Long clienteEmp_id);

    @Query("SELECT e FROM Elementos e WHERE e.codigo = :codigo AND e.clienteAsp_id = :clienteAsp_id AND e.clienteEmp_id = :clienteEmp_id")
    Elementos findFirstByCodigoAndClienteAsp_idAndClienteEmp_id(@Param("codigo") String codigo, @Param("clienteAsp_id") Long clienteAsp_id, @Param("clienteEmp_id") Long clienteEmp_id);
    //Elementos findByCodigoAndClienteAsp_id(String codigo, Long clienteAsp_id);

    @Query( "SELECT o FROM Elementos o WHERE o.estado = :idReq " )
    List<Elementos> findByReqId(@Param("idReq") String idReq);

    @Query( "SELECT o.codigo FROM Elementos o WHERE o.id= :idELem " )
    String findByIdString (@Param("idELem") Long idELem);

    @Query( "SELECT o.codigo" +
            "  FROM Elementos o " +
            "  WHERE o.id IN :idEle " )
    List<String> findByElemId(@Param("idEle") List<Long> idEle);

    @Query( "SELECT o FROM Elementos o " +
            "  WHERE o.contenedor_id = :idEle " )
    List<Elementos> findContenidos(@Param("idEle") Long idEle);

    @Query( "SELECT o FROM Elementos o " +
            "  WHERE o.contenedor_id = :idEle and  o.estado = 'En Guarda' " )
    List<Elementos> findContenidos1(@Param("idEle") Long idEle);

    Elementos findByCodigoAndClienteEmp_id(String codigo, Long idCliEmp);

    Elementos findFirstById(Long id);

    @Query("SELECT id FROM Elementos WHERE codigo = :codigo AND clienteAsp_id = :clienteAsp")
    Long findIdByCodigoAndClienteAsp(@Param("codigo") String codigo, @Param("clienteAsp") Long clienteAsp);

 //   @Query("SELECT id  ,codigo , estado  FROM Elementos  WHERE codigo like :cod ")
 //   List<Elementos> findByCodService(@Param("cod") String cod) ;

   // @Query(value = "select * from elementos r\n" +
   //         "inner join posiciones o      on r.posicion_id      = o.id        \n" +
   //         "where r.codigo = :cod", nativeQuery = true)
 //   List<Elementos> findByCodService(@Param("cod") String cod) ;

    @Query(value = "select * from elementos r\n" +
            "LEFT OUTER JOIN posiciones o      on r.posicion_id      = o.id        \n" +
            "where r.codigo = :cod", nativeQuery = true)
    List<Elementos> findByCodService(@Param("cod") String cod) ;




}
