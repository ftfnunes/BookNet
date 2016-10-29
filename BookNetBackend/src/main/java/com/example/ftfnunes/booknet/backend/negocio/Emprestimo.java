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
}
