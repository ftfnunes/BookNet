package com.example.ftfnunes.booknet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class telaLogin extends Activity {

    private EditText campoLogin, campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
    }

    public void login(View v){
        campoLogin = (EditText) findViewById(R.id.campoLogin);
        campoSenha = (EditText) findViewById(R.id.campoSenha);

        /* Para utilizar as strings desses campos, basta utilizar:
        *
        * Ex.: campoLogin.getText().toString();
        *
        * */

        Intent it = new Intent(this, telaInicio.class);
        startActivity(it);
    }

    public void registro(View v){
        Intent it = new Intent(this, TelaRegistro.class);
        startActivity(it);
    }

}
