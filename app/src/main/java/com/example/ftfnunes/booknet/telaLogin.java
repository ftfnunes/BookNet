package com.example.ftfnunes.booknet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class telaLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
    }

    public void login(View v){
        Intent it = new Intent(this, telaInicio.class);
        startActivity(it);
    }

    public void registro(View v){
        Intent it = new Intent(this, TelaRegistro.class);
        startActivity(it);
    }

}
