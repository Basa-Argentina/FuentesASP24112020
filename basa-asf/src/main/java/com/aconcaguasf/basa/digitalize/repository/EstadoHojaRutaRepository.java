package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.EstadoHojaRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoHojaRutaRepository extends JpaRepository<EstadoHojaRuta, Long> {
    EstadoHojaRuta findById(Long id);
}
