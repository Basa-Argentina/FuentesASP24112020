package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "x_user_group")
public class UsersxGrupo {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private Integer group_id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private UsuariosPlanta usuariosPlanta;

    public UsuariosPlanta getUsuariosPlanta() {
        return usuariosPlanta;
    }

    public void setUsuariosPlanta(UsuariosPlanta usuariosPlanta) {
        this.usuariosPlanta = usuariosPlanta;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

}




