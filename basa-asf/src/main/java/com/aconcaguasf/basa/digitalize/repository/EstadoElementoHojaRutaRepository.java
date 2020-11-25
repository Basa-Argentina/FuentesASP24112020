package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.EstadoElementoHojaRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoElementoHojaRutaRepository extends JpaRepository<EstadoElementoHojaRuta, Long> {
    EstadoElementoHojaRuta findEstadoElementoHojaRutaById(Long id);
}
