package com.example.ftfnunes.booknet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class TelaRegistro extends AppCompatActivity {

    private EditText nomeRegistro, loginRegistro, senhaRegistro, emailRegistro, telRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nomeRegistro = (EditText) findViewById(R.id.nomeRegistro);
        loginRegistro = (EditText) findViewById(R.id.loginRegistro);
        senhaRegistro = (EditText) findViewById(R.id.senhaRegistro);
        emailRegistro = (EditText) findViewById(R.id.emailRegistro);
        telRegistro = (EditText) findViewById(R.id.telRegistro);

        final Button regButton = (Button)findViewById(R.id.button3);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario();
                usuario.setNome(nomeRegistro.getText().toString());
                usuario.setUserName(loginRegistro.getText().toString());
                usuario.setAvaliacao(0);
                usuario.setEmprestimosFeitos(0);

                new SalvaUsuarioAsync(TelaRegistro.this).execute(usuario);
            }
        });

    }

    public void voltaLogin(View v){
        Intent it = new Intent(this, telaLogin.class);
        startActivity(it);
    }

    public void registrar(View v){
        nomeRegistro = (EditText) findViewById(R.id.nomeRegistro);
        loginRegistro = (EditText) findViewById(R.id.loginRegistro);
        senhaRegistro = (EditText) findViewById(R.id.senhaRegistro);
        emailRegistro = (EditText) findViewById(R.id.emailRegistro);
        telRegistro = (EditText) findViewById(R.id.telRegistro);


        Intent it = new Intent(this, telaLogin.class);
        startActivity(it);
    }

    private class SalvaUsuarioAsync extends AsyncTask<Usuario, Void, Usuario> {
        Context context;
        private ProgressDialog pd;



        public SalvaUsuarioAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Salvando Usuario..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }


        protected Usuario doInBackground(Usuario... usuario) {
            /* São criadas Colection de avaliações e correções para armazenar o resultado das buscas n banco de dados.*/

            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */
            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null)
                        .setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service =  builder.build();

                return service.addUsuario(usuario[0]).execute();
            }
            catch(Exception ex){
                /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            Toast.makeText(getApplicationContext(),"Anuncio Salvo",Toast.LENGTH_SHORT).show();
            pd.dismiss();

        }
    }

}
