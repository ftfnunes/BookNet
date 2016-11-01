package com.example.ftfnunes.booknet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class AtividadesDisponibilizeiFragment extends Fragment {
    Usuario usuario;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atividades_disponibilizei, container, false);
    }

}

