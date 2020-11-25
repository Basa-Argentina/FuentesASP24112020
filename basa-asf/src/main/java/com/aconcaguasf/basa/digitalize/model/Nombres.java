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

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users")
public class Nombres {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private Long persona_id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="persona_id", insertable=false, updatable=false)
    private PersonasFisicas personasFisicas;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="persona_id", insertable=false, updatable=false)
    private PersonasJuridicas personasJuridicas;


    public PersonasJuridicas getPersonasJuridicas() {
        return personasJuridicas;
    }

    public void setPersonasJuridicas(PersonasJuridicas personasJuridicas) {
        this.personasJuridicas = personasJuridicas;
    }

    public PersonasFisicas getPersonasFisicas() {
        return personasFisicas;
    }

    public void setPersonasFisicas(PersonasFisicas personasFisicas) {
        this.personasFisicas = personasFisicas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(Long persona_id) {
        this.persona_id = persona_id;
    }
}
