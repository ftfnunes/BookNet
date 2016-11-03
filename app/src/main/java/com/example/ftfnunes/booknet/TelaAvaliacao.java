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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import de.greenrobot.event.EventBus;

public class TelaAvaliacao extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Usuario usuario;
    private TextView nomeUsuarioHeader, emailUsuarioHeader;
    RatingBar rb;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_avaliacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuario = EventBus.getDefault().getStickyEvent(Usuario.class);

        rb = (RatingBar)findViewById(R.id.ratingBar2);
        button = (Button)findViewById(R.id.buttonEnviar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.setEmprestimosFeitos(usuario.getEmprestimosFeitos()+1);
                usuario.setAvaliacao((usuario.getAvaliacao()+(int)rb.getRating())/(usuario.getEmprestimosFeitos()));
                new SalvaUsuarioAsync(TelaAvaliacao.this).execute(usuario);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        nomeUsuarioHeader = (TextView) findViewById(R.id.nomeUsuarioHeader);
        nomeUsuarioHeader.setText(usuario.getNome());
        emailUsuarioHeader = (TextView) findViewById(R.id.emailUsuarioHeader);
        emailUsuarioHeader.setText(usuario.getUserName());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
            Intent it = new Intent(this, TelaAtividades.class);
            de.greenrobot.event.EventBus.getDefault().postSticky(usuario);
            startActivity(it);
        } else if (id == R.id.men_lat_sair) {
            Intent it = new Intent(this, telaLogin.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            TelaAvaliacao.this.setResult(RESULT_OK, null);
            TelaAvaliacao.this.finish();
        }
    }

}
