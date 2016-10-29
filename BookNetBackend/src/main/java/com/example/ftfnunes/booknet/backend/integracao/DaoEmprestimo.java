package com.example.ftfnunes.booknet.backend.integracao;

import com.example.ftfnunes.booknet.backend.negocio.Anuncio;
import com.example.ftfnunes.booknet.backend.negocio.Emprestimo;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by ftfnunes on 29/10/16.
 */

public class DaoEmprestimo {


    public List<Emprestimo> recuperaPorAnunciante(String anunciante){
        List<Emprestimo> anuncios = ofy().load().type(Emprestimo.class).filter("anunciante.userName", anunciante).list();
        return anuncios;
    }

    public List<Emprestimo> recuperaPorInteressado(String anunciante){
        List<Emprestimo> anuncios = ofy().load().type(Emprestimo.class).filter("interessado.userName", anunciante).list();
        return anuncios;
    }
    public void salvaEmprestimo(Emprestimo emprestimo){
        ofy().save().entity(emprestimo).now();
    }
}
