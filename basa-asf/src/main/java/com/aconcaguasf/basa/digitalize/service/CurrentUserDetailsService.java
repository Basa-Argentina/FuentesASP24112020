package com.aconcaguasf.basa.digitalize.service;

import com.aconcaguasf.basa.digitalize.model.RolesGrupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aconcaguasf.basa.digitalize.model.Users;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public CurrentUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public CurrentUser loadUserByUsername(String usrname) throws UsernameNotFoundException {
		Users users = userService.getUserByUsername(usrname).orElseThrow(
				() -> new UsernameNotFoundException(String.format("Users was not found", usrname)));
        ArrayList<String> gruposTemp = new ArrayList<>();
        List<SimpleGrantedAuthority> returnlist = new ArrayList<SimpleGrantedAuthority>();
		for(RolesGrupo rolesGrupo : users.getGruposRoles()) {
            returnlist.add(new SimpleGrantedAuthority(rolesGrupo.getGroup_id().toString()));
		}
		users.setGrupos(returnlist);
		return new CurrentUser(users);
	}

}
