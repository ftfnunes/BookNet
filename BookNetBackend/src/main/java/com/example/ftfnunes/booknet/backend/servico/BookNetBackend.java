/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.ftfnunes.booknet.backend.servico;

import com.example.ftfnunes.booknet.backend.integracao.DaoAnuncio;
import com.example.ftfnunes.booknet.backend.integracao.DaoEmprestimo;
import com.example.ftfnunes.booknet.backend.integracao.DaoUsuario;
import com.example.ftfnunes.booknet.backend.negocio.Anuncio;
import com.example.ftfnunes.booknet.backend.negocio.Emprestimo;
import com.example.ftfnunes.booknet.backend.negocio.Usuario;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "bookNetBackend",
        version = "v1",
        description = "API para gerenciar os acessos aos objetos de emprestimo, usuarios e anuncios"
)
public class BookNetBackend {
    DaoAnuncio daoAnuncio= new DaoAnuncio(); /**< Objeto do tipo DAONota, para permitir as operações com banco de dados com objetos do tipo Nota.*/
    DaoUsuario daoUsuario = new DaoUsuario();
    DaoEmprestimo daoEmprestimo = new DaoEmprestimo();

    public void init(){
        ObjectifyService.begin();
        ObjectifyService.register(Usuario.class);
        ObjectifyService.register(Anuncio.class);
        ObjectifyService.register(Emprestimo.class);
    }

    @ApiMethod(name="addUsuario")
    public Usuario salvaUsuario(Usuario usuario){
        init();
        daoUsuario.salvaUsuario(usuario);
        return usuario;
    }

    @ApiMethod(name="addAnuncio")
    public Anuncio salvaAnuncio(Anuncio anuncio){
        init();
        daoAnuncio.salvaAnuncio(anuncio);
        return anuncio;
    }


    @ApiMethod(name="addEmprestimo")
    public Emprestimo salvaEmprestimo(Emprestimo emprestimo){
        init();
        daoEmprestimo.salvaEmprestimo(emprestimo);
        return emprestimo;
    }

    @ApiMethod(name="getUsuario")
    public Usuario recuperaUsuario(@Named("userName") String userName){
        init();
        return daoUsuario.recuperaUsuario(userName);
    }

    @ApiMethod(name="listPorAnunciante")
    public List<Anuncio> recuperaAnunciosPorAnunciante(@Named("userName") String anunciante){
        init();
        return daoAnuncio.recuperaPorAnunciante(anunciante);
    }


    @ApiMethod(name="listPorTitulo")
    public List<Anuncio> recuperaAnunciosPorTitulo(@Named("titulo") String titulo){
        init();
        return daoAnuncio.recuperaPorTitulo(titulo);
    }

    @ApiMethod(name="listPorAutor")
    public List<Anuncio> recuperaAnunciosPorAutor(@Named("autor") String autor){
        init();
        return daoAnuncio.recuperaPorAutor(autor);
    }

    @ApiMethod(name="listPorGenero")
    public List<Anuncio> recuperaAnunciosPorGenero(@Named("genero") String genero){
        init();
        return daoAnuncio.recuperaPorGenero(genero);
    }

    @ApiMethod(name="listEmpPorAnun")
    public List<Emprestimo> recuperaEmprestimoPorAnunciante(@Named("anunciante") String anunciante){
        init();
        return daoEmprestimo.recuperaPorAnunciante(anunciante);
    }
    @ApiMethod(name="listEmpPorInt")
    public List<Emprestimo> recuperaEmprestimoPorInteressado(@Named("interessado") String interessado){
        init();
        return daoEmprestimo.recuperaPorInteressado(interessado);
    }
}
