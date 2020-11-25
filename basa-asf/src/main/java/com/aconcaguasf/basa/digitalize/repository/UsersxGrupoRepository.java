package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.UsersxGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersxGrupoRepository extends JpaRepository<UsersxGrupo, Long> {

    @Query(value = "SELECT a FROM UsersxGrupo a where a. group_id = :group ")
    List<UsersxGrupo> findByGroup (@Param("group") Integer group);

}