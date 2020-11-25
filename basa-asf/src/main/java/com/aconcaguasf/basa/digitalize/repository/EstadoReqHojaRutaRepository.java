package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.EstadoReqHojaRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReqHojaRutaRepository extends JpaRepository<EstadoReqHojaRuta, Long> {
    EstadoReqHojaRuta findById(Long id);
}
