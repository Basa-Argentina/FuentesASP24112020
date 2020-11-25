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

@Entity
@Table(name = "posiciones")
public class Posiciones {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Integer posHorizontal;

    @Column(nullable = false)
    private Integer posVertical;

    @Column(nullable = false)
    private Long estante_id;

    @Column(nullable = false)
    private Long modulo_id;

    @ManyToOne
    @JoinColumn(name="estante_id", insertable=false, updatable=false)
    private Estanterias estanteria;

    @ManyToOne
    @JoinColumn(name="modulo_id", insertable=false, updatable=false)
    private Modulos modulo;

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

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPosHorizontal() {
        return posHorizontal;
    }
    public void setPosHorizontal(Integer posHorizontal) {
        this.posHorizontal = posHorizontal;
    }

    public Integer getPosVertical() {
        return posVertical;
    }
    public void setPosVertical(Integer posVertical) {
        this.posVertical = posVertical;
    }

    public Long getEstante_id() {
        return estante_id;
    }
    public void setEstante_id(Long estante_id) {
        this.estante_id = estante_id;
    }

    public Long getModulo_id() {
        return modulo_id;
    }
    public void setModulo_id(Long modulo_id) {
        this.modulo_id = modulo_id;
    }

    public Estanterias getEstanteria() {
        return estanteria;
    }
    public void setEstanteria(Estanterias estanteria) {
        this.estanteria = estanteria;
    }

    public Modulos getModulo() {
        return modulo;
    }
    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }
}
