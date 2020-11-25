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

import javax.persistence.*;

@Entity
@Table(name = "estanterias")
public class Estanterias {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private Long grupo_id;

    @Column(nullable = false)
    private Long tipoJerarquia_id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getGrupo_id() {
        return grupo_id;
    }
    public void setGrupo_id(Long grupo_id) {
        this.grupo_id = grupo_id;
    }

    public Long getTipoJerarquia_id() {
        return tipoJerarquia_id;
    }
    public void setTipoJerarquia_id(Long tipoJerarquia_id) {
        this.tipoJerarquia_id = tipoJerarquia_id;
    }
}
