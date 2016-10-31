package com.example.ftfnunes.booknet.backend.negocio;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by ftfnunes on 29/10/16.
 */

@Entity
public class Emprestimo {
    @Id Long id;
    @Index Usuario anunciante;
    @Index Usuario interessado;
    @Index Anuncio anuncio;
    Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAnunciante() {
        return anunciante;
    }

    public void setAnunciante(Usuario anunciante) {
        this.anunciante = anunciante;
    }

    public Usuario getInteressado() {
        return interessado;
    }

    public void setInteressado(Usuario interessado) {
        this.interessado = interessado;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
