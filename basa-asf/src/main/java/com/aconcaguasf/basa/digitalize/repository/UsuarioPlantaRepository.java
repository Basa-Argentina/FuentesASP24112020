package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.UsuariosPlanta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioPlantaRepository extends JpaRepository<UsuariosPlanta, Long> {

    UsuariosPlanta findByUsername(String username);

}
