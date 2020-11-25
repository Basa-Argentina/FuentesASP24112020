package com.aconcaguasf.basa.digitalize.model;
/*
 *
 *  Copyright (c) 2017./ Aconcagua SF S.A.
 *  *
 *  Licensed under the Goycoolea inc License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://crossover.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author 		: Hector Goycoolea
 *  @developer		: Hector Goycoolea
 *
 *  Date Changes
 *  02/09/17 00:21:40 Argentina Timezone
 *
 *  Changes :
 *
 *  email column
 */


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private Boolean admin;

	@Column(nullable = false)
	private Boolean enable;

	@Column(nullable = false, name = "lastLogin")
	private String lastLogin;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Date passwordChangeDate;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private Long cliente_id;

	@Column(nullable = false)
	private Long persona_id;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="user_id")
    private Set<RolesGrupo> gruposRoles;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="persona_id", insertable=false, updatable=false)
    private Personas personas;

       @Transient
    private List<SimpleGrantedAuthority> grupos;

    public Set<RolesGrupo> getGruposRoles() {
        return gruposRoles;
    }

    public void setGruposRoles(Set<RolesGrupo> gruposRoles) {
        this.gruposRoles = gruposRoles;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    public List<SimpleGrantedAuthority> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<SimpleGrantedAuthority> grupos) {
        this.grupos = grupos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Long cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Long getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(Long persona_id) {
        this.persona_id = persona_id;
    }


}
