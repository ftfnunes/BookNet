package com.example.ftfnunes.booknet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.AnuncioCollection;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class telaLogin extends Activity {
    private EditText campoLogin, campoSenha;
    private Button botaoLogin;
    private ImageView imagemLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        botaoLogin = (Button) findViewById(R.id.botaoEntrarLogin);
        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoLogin = (EditText) findViewById(R.id.campoLogin);
                campoSenha = (EditText) findViewById(R.id.campoSenha);

        /* Para utilizar as strings desses campos, basta utilizar:
        *
        * Ex.: campoLogin.getText().toString();
        *
        * */

                new RecuperaLoginAsync(telaLogin.this).execute(campoLogin.getText().toString());
            }
        });

        imagemLogin = (ImageView) findViewById(R.id.imagemLogin);
        imagemLogin.setVisibility(View.VISIBLE);

    }

    public void avaliaLogin(Usuario usuario){
        if(usuario != null){
            Intent it = new Intent(telaLogin.this, telaInicio.class);
            de.greenrobot.event.EventBus.getDefault().postSticky(usuario);
            startActivity(it);
        }
        else {
            Toast.makeText(telaLogin.this,"Usuário não encontrado.", Toast.LENGTH_LONG).show();
        }
    }


    public void registro(View v) {
        Intent it = new Intent(this, TelaRegistro.class);
        startActivity(it);
    }


    private class RecuperaLoginAsync extends AsyncTask<String, Void, Usuario> {
        Context context;
        private ProgressDialog pd;
        private Usuario user;

        public RecuperaLoginAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Buscando verificando login..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }

        protected Usuario doInBackground(String... username) {

            try {
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null)
                        .setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service = builder.build();
                user = service.getUsuario(username[0]).execute();
            } catch (Exception ex) {
                    /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }

            return user;
        }

        protected void onPostExecute(final Usuario usuario) {
            avaliaLogin(usuario);
            pd.dismiss();
        }

    }


}
