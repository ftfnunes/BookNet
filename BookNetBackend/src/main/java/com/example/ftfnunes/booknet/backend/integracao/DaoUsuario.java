package com.example.ftfnunes.booknet.backend.integracao;

/**
 * Created by ftfnunes on 28/10/16.
 */

import com.example.ftfnunes.booknet.backend.negocio.Usuario;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DaoUsuario {

    public Usuario recuperaUsuario(String userName){
        List<Usuario> usuario = ofy().load().type(Usuario.class).filter("userName", userName).list();
        return usuario.isEmpty() ? null : usuario.get(0);
    }

    public void salvaUsuario(Usuario usuario){
        ofy().save().entity(usuario).now();
    }
}
