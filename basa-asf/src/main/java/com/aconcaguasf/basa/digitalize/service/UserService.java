package com.aconcaguasf.basa.digitalize.service;

import java.util.Optional;

import com.aconcaguasf.basa.digitalize.model.Users;

public interface UserService {

    Optional<Users> getUserByUsername(String username);

}