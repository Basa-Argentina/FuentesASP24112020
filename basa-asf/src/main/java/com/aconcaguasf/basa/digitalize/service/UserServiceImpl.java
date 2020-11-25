package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.Users;
import com.aconcaguasf.basa.digitalize.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Users> getUserByUsername(String username) {
		return Optional.ofNullable(userRepository.findByUsername(username));
	}
}
