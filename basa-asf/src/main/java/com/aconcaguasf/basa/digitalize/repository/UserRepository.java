package com.aconcaguasf.basa.digitalize.repository;

import com.aconcaguasf.basa.digitalize.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aconcaguasf.basa.digitalize.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    @Query(value = "SELECT   View_clienteEmpleados.mail\n" +
            "FROM            View_clienteEmpleados INNER JOIN\n" +
            "                         users ON View_clienteEmpleados.clienteEmpleados_ID = users.id\n" +
            "WHERE        (View_clienteEmpleados.clienteEmpleados_ID = :username)  ", nativeQuery = true)
    String findemailUser(@Param("username") Long username);



}

