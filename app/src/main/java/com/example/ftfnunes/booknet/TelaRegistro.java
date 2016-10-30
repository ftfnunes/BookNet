package com.example.ftfnunes.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TelaRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void voltaLogin(View v){
        Intent it = new Intent(this, telaLogin.class);
        startActivity(it);
    }

    public void registrar(View v){

        /* Colocar métodos que realizam o registro dos usuários. */

        Intent it = new Intent(this, telaLogin.class);
        startActivity(it);
    }

}
