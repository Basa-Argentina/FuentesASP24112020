package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.ClienteEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteEmpRepository extends JpaRepository<ClienteEmp, Long> {
    @Query(value = "SELECT a.personasJuridicas , a.codigo FROM ClienteEmp a ")
    List<Object> findPersonsaJuridicas();


}
