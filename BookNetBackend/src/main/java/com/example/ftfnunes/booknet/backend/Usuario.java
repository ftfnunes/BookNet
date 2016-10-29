package com.example.ftfnunes.booknet.backend;

/**
 * Created by ftfnunes on 28/10/16.
 */

public class Usuario {
    String userName;
    String nome;
    int avaliacao;
    int emprestimosFeitos;


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
