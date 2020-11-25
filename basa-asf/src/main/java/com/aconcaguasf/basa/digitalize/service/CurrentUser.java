package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.Users;
public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private Users users;

	public CurrentUser(Users users) {

		super(users.getUsername(), users.getPassword(), users.getGrupos());
		this.users = users;

	}


	public Users getUsers() {
		return users;
	}

	public Long getId() {
		return users.getId();
	}

}