package com.aconcaguasf.basa.digitalize.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by acorrea on 03/07/2017.
 */
@Entity
@Table(name = "mensajes")
public class Mensajes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long requerimiento_id;

    @Column(nullable = false)
    private Long usrOrigen_id;

    @Column(nullable = false)
    private Long usrDestino_id;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private Date fechaLeido;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private Boolean leido;

    @Column(nullable = false)
    private Boolean eliminado;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="usrOrigen_id", insertable=false, updatable=false)
    private Nombres usrOrigen;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="usrDestino_id", insertable=false, updatable=false)
    private Nombres usrDestino;

    public Long getId() {
        return id;
    }

    public Nombres getUsrOrigen() {
        return usrOrigen;
    }

    public void setUsrOrigen(Nombres usrOrigen) {
        this.usrOrigen = usrOrigen;
    }

    public Nombres getUsrDestino() {
        return usrDestino;
    }

    public void setUsrDestino(Nombres usrDestino) {
        this.usrDestino = usrDestino;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequerimiento_id() {
        return requerimiento_id;
    }

    public void setRequerimiento_id(Long requerimiento_id) {
        this.requerimiento_id = requerimiento_id;
    }

    public Long getUsrOrigen_id() {
        return usrOrigen_id;
    }

    public void setUsrOrigen_id(Long usrOrigen_id) {
        this.usrOrigen_id = usrOrigen_id;
    }

    public Long getUsrDestino_id() {
        return usrDestino_id;
    }

    public void setUsrDestino_id(Long usrDestino_id) {
        this.usrDestino_id = usrDestino_id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(Date fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
