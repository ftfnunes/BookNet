package com.example.ftfnunes.booknet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class telaAnuncio extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_anuncio);
    }




    public void setMenu(View v){
        /* Lembrar de trocar MainActivity para a tela desejada. */
        Intent it = new Intent(telaAnuncio.this, MainActivity.class);
        startActivity(it);
    }
}
