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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_legacy")

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private String username;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="user_id")
    private Set<RolesGrupo> gruposRoles;

    @Transient
    private List<SimpleGrantedAuthority> grupos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RolesGrupo> getGruposRoles() {
        return gruposRoles;
    }

    public void setGruposRoles(Set<RolesGrupo> gruposRoles) {
        this.gruposRoles = gruposRoles;
    }

    public List<SimpleGrantedAuthority> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<SimpleGrantedAuthority> grupos) {
        this.grupos = grupos;
    }
}
