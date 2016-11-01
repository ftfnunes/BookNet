package com.example.ftfnunes.booknet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.AnuncioCollection;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class telaAnuncio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText nomeLivroAnuncio, autorLivroAnuncio, generoLivroAnuncio, edicaoLivroAnuncio, descLivroAnuncio;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_anuncio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button)findViewById(R.id.botaoEnviarAnuncio);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anuncio anuncio = new Anuncio();
                anuncio.setNomeDoLivro(nomeLivroAnuncio.getText().toString());
                anuncio.setAutor(autorLivroAnuncio.getText().toString());
                anuncio.setGenero(generoLivroAnuncio.getText().toString());
                anuncio.setEdicao(Integer.parseInt(edicaoLivroAnuncio.getText().toString()));
                anuncio.setDescricao(descLivroAnuncio.getText().toString());
                anuncio.setAnunciante(usuario);
                new SalvaAnuncioAsync(telaAnuncio.this).execute(anuncio);
            }
        });

        nomeLivroAnuncio = (EditText) findViewById(R.id.nomeLivroAnuncio);
        autorLivroAnuncio = (EditText) findViewById(R.id.autorLivroAnuncio);
        generoLivroAnuncio = (EditText) findViewById(R.id.generoLivroAnuncio);
        edicaoLivroAnuncio = (EditText) findViewById(R.id.edicaoLivroAnuncio);
        descLivroAnuncio = (EditText) findViewById(R.id.descLivroAnuncio);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        usuario = (Usuario) EventBus.getDefault().getStickyEvent(Usuario.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.men_lat_inicio) {
            Intent it = new Intent(this, telaInicio.class);
            startActivity(it);
        } else if (id == R.id.men_lat_chat) {

        } else if (id == R.id.men_lat_hist) {

        } else if (id == R.id.men_lat_myprof) {

        } else if (id == R.id.men_lat_not) {

        } else if (id == R.id.men_lat_solic) {
            Intent it = new Intent(this, AprovacaoDeSolicitacao.class);
            startActivity(it);
        } else if (id == R.id.men_lat_sair) {
            Intent it = new Intent(this, telaLogin.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class SalvaAnuncioAsync extends AsyncTask<Anuncio, Void, Anuncio> {
        Context context;
        private ProgressDialog pd;



        public SalvaAnuncioAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Buscando Avaliacões..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }


        protected Anuncio doInBackground(Anuncio... anuncio) {
            /* São criadas Colection de avaliações e correções para armazenar o resultado das buscas n banco de dados.*/

            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */
            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
                builder.setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service = builder.build();

                return service.addAnuncio(anuncio[0]).execute();
            }
            catch(Exception ex){
                /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }

            return null;

        }


        @Override
        protected void onPostExecute(Anuncio anuncio) {
            Toast.makeText(getApplicationContext(),"Anuncio Salvo",Toast.LENGTH_SHORT).show();
            pd.dismiss();

        }
    }
}
