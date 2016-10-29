package com.example.ftfnunes.booknet.backend.integracao;

import com.example.ftfnunes.booknet.backend.negocio.Anuncio;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by ftfnunes on 28/10/16.
 */

public class DaoAnuncio {
    public List<Anuncio> recuperaPorAnunciante(String anunciante){
        List<Anuncio> anuncios = ofy().load().type(Anuncio.class).filter("anunciante.userName", anunciante).list();
        return anuncios;
    }

    public List<Anuncio> recuperaPorTitulo(String titulo){
        List<Anuncio> anuncios = ofy().load().type(Anuncio.class).filter("nomeDoLivro", titulo).list();
        return anuncios;
    }

    public List<Anuncio> recuperaPorAutor(String autor){
        List<Anuncio> anuncios = ofy().load().type(Anuncio.class).filter("autor", autor).list();
        return anuncios;
    }
    public List<Anuncio> recuperaPorGenero(String genero){
        List<Anuncio> anuncios = ofy().load().type(Anuncio.class).filter("genero", genero).list();
        return anuncios;
    }
    public void salvaAnuncio(Anuncio anuncio){
        ofy().save().entity(anuncio).now();
    }
}
