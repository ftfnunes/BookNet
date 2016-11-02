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
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.util.concurrent.ExecutionException;

import de.greenrobot.event.EventBus;

public class AprovacaoDeSolicitacao extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Usuario usuario;
    Emprestimo emprestimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprovacao_de_solicitacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuario = EventBus.getDefault().getStickyEvent(Usuario.class);
        emprestimo = EventBus.getDefault().getStickyEvent(Emprestimo.class);

        TextView tituloText = (TextView) findViewById(R.id.TituloText);
        TextView autorText = (TextView) findViewById(R.id.textAutor);
        TextView edText = (TextView) findViewById(R.id.textGenero);
        TextView userText = (TextView) findViewById(R.id.textNomeUsuario);
        TextView telText = (TextView) findViewById(R.id.textTelefone);

        tituloText.setText(emprestimo.getAnuncio().getNomeDoLivro());
        autorText.setText(emprestimo.getAnuncio().getAutor());
        edText.setText(emprestimo.getAnuncio().getEdicao() + " Edicao");
        userText.setText(emprestimo.getInteressado().getUserName());
        telText.setText("3212344");
        /*Trocar para campo telefone quando for adicionado a entidade Usuario*/
        Button aceitarButton = (Button)findViewById(R.id.buttonAceitar);
        Button recusarButton = (Button)findViewById(R.id.buttonRecusar);


        aceitarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emprestimo.setStatus("Emprestado");
                new SalvaEmprestimoAsync(AprovacaoDeSolicitacao.this).execute(emprestimo);
            }
        });

        recusarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emprestimo.setStatus("Recusado");
                try {
                    new SalvaEmprestimoAsync(AprovacaoDeSolicitacao.this).execute(emprestimo).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
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
        getMenuInflater().inflate(R.menu.aprovacao_de_solicitacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

    private class SalvaEmprestimoAsync extends AsyncTask<Emprestimo, Void, Emprestimo> {
        Context context;
        private ProgressDialog pd;



        public SalvaEmprestimoAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Salvando Solicitacao..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }


        protected Emprestimo doInBackground(Emprestimo... emprestimo) {
            /* São criadas Colection de avaliações e correções para armazenar o resultado das buscas n banco de dados.*/

            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */
            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null)
                        .setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service =  builder.build();
                emprestimo[0].getAnuncio().setValid(false);
                return service.addEmprestimo(emprestimo[0]).execute();
            }
            catch(Exception ex){
                /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Emprestimo emprestimo) {
            pd.dismiss();
            new SalvaAnuncioAsync(AprovacaoDeSolicitacao.this).execute(emprestimo.getAnuncio());
        }
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
            Toast.makeText(getApplicationContext(),"Solicitacao de Emprestimo Salva",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("RESULT_AP", emprestimo.getStatus() == "Recusado" ? "Recusado" : "Aprovado");
            EventBus.getDefault().postSticky(emprestimo);
            AprovacaoDeSolicitacao.this.setResult(RESULT_OK, intent);
            pd.dismiss();
            AprovacaoDeSolicitacao.this.finish();
        }
    }
}
