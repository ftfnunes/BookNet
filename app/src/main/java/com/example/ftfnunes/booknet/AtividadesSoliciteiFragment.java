package com.example.ftfnunes.booknet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appspot.myapplicationid.bookNetBackend.model.Usuario;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class AtividadesSoliciteiFragment extends Fragment {
    Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atividades_solicitei, container, false);


        // Inflate the layout for this fragment
        return view;
    }

}
