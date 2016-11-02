package com.example.ftfnunes.booknet.backend.negocio;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by ftfnunes on 28/10/16.
 */

@Entity
public class Usuario {
    @Id Long id;
    @Index String userName;
    String nome;
    int avaliacao;
    int emprestimosFeitos=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getEmprestimosFeitos() {
        return emprestimosFeitos;
    }

    public void setEmprestimosFeitos(int emprestimosFeitos) {
        this.emprestimosFeitos = emprestimosFeitos;
    }


}
