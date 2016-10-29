package com.example.ftfnunes.booknet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class telaInicio extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);
    }

    public void setTelaAnunciar(View v){
        /* Lembrar de trocar MainActivity para a tela desejada. */
        Intent it = new Intent(telaInicio.this, telaAnuncio.class);
        startActivity(it);
    }

    public void setTelaProcurar(View v){
        /* Lembrar de trocar MainActivity para a tela desejada. */
        Intent it = new Intent(telaInicio.this, MainActivity.class);
        startActivity(it);
    }

    public void setMenu(View v){
        /* Lembrar de trocar MainActivity para a tela desejada. */
        Intent it = new Intent(telaInicio.this, MainActivity.class);
        startActivity(it);
    }


}