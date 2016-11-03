package com.example.ftfnunes.booknet;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.AnuncioCollection;
import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;
import com.appspot.myapplicationid.bookNetBackend.model.EmprestimoCollection;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.appspot.myapplicationid.bookNetBackend.model.Usuario;

import de.greenrobot.event.EventBus;

public class TelaAtividades extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int APROVAR_SOLICITACAO = 1;
    private static final int GET_AVAL = 2;
    EmprestimosAdapter soliciteiAdapter;
    EmprestimosAdapter disponibilizeiAdapter;
    ListView listView;
    ArrayList<Emprestimo> disponibilizados;
    private Usuario usuario;
    private TextView nomeUsuarioHeader, emailUsuarioHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_atividades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Atributos que serao usados pela ASyncTask*/
        usuario = (Usuario) EventBus.getDefault().getStickyEvent(Usuario.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        listView = (ListView)findViewById(R.id.atList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emprestimo emprestimo = (Emprestimo) parent.getItemAtPosition(position);
                if(emprestimo.getStatus().equals("Emprestado")) {
                    Intent intent = new Intent(TelaAtividades.this, StatusEmprestimo.class);
                    EventBus.getDefault().postSticky(emprestimo);
                    startActivity(intent);
                }
                else if(emprestimo.getStatus().equals("Finalizado")){
                    Intent intent = new Intent(TelaAtividades.this, TelaAvaliacao.class);
                    EventBus.getDefault().postSticky(emprestimo.getAnunciante());
                    startActivityForResult(intent, GET_AVAL);
                }
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Solicitei"));
        tabLayout.addTab(tabLayout.newTab().setText("Disponibilizei"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString() == "Solicitei"){
                    listView.setAdapter(soliciteiAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Emprestimo emprestimo = (Emprestimo) parent.getItemAtPosition(position);
                            if(emprestimo.getStatus().equals("Emprestado")) {
                                Intent intent = new Intent(TelaAtividades.this, StatusEmprestimo.class);
                                EventBus.getDefault().postSticky(emprestimo);
                                startActivity(intent);
                            }
                            else if(emprestimo.getStatus().equals("Finalizado")){
                                Intent intent = new Intent(TelaAtividades.this, TelaAvaliacao.class);
                                EventBus.getDefault().postSticky(emprestimo.getAnunciante());
                                startActivityForResult(intent, GET_AVAL);
                            }
                        }
                    });
                }
                else {
                    listView.setAdapter(disponibilizeiAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Emprestimo emprestimo = (Emprestimo) parent.getItemAtPosition(position);
                            if (emprestimo.getStatus().equals("Pendente")) {
                                EventBus.getDefault().postSticky(emprestimo);
                                Intent intent = new Intent(TelaAtividades.this, AprovacaoDeSolicitacao.class);
                                TelaAtividades.this.startActivityForResult(intent, APROVAR_SOLICITACAO);
                            } else if(emprestimo.getStatus().equals("Emprestado")){
                                EventBus.getDefault().postSticky(emprestimo);
                                Intent intent = new Intent(TelaAtividades.this, LivroEmprestado.class);
                                startActivity(intent);
                            }

                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        new RecuperaEmprestimosAsync(this).execute(usuario.getUserName());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_AVAL && resultCode == RESULT_OK){
            EventBus.getDefault().postSticky(usuario);
        }
        else if (resultCode == RESULT_OK && data.getStringExtra("RESULT_AP").equals("Aprovado") && requestCode == APROVAR_SOLICITACAO){
            Emprestimo resultEmprestimo = EventBus.getDefault().removeStickyEvent(Emprestimo.class);
            Long id1, id2;
            for (Emprestimo emprestimo : disponibilizados) {
                if (emprestimo.getAnuncio().equals(resultEmprestimo.getAnuncio()) && !emprestimo.getId().equals(resultEmprestimo.getId())){
                    emprestimo.setStatus("Recusado");
                    new SalvaEmprestimoAsync(this).execute(emprestimo);
                }
            }
        }
    }

    private class RecuperaEmprestimosAsync extends AsyncTask<String, Void, List<Object>>{
        Context context;
        private ProgressDialog pd;


        public RecuperaEmprestimosAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Buscando Avaliacões..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }


        protected List<Object> doInBackground(String... username) {
            /* São criadas Colection de emprestimos para armazenar o resultado das buscas n banco de dados.*/
            EmprestimoCollection emprestimosFeitos = new EmprestimoCollection();
            EmprestimoCollection emprestimosSolicitados = new EmprestimoCollection();
            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */

            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
                builder.setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service =  builder.build();
                emprestimosFeitos = service.listEmpPorAnun(username[0] != null ? username[0] : "").execute();
                emprestimosSolicitados = service.listEmpPorInt(username[0] != null ? username[0] : "").execute();
            }
            catch(Exception ex){
                /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }

            List<Object> resultados = new ArrayList<>();
            resultados.add(emprestimosFeitos.getItems() == null  ? new ArrayList<Emprestimo>() : emprestimosFeitos.getItems());
            resultados.add(emprestimosSolicitados.getItems() == null ? new ArrayList<Emprestimo>() : emprestimosSolicitados.getItems());
            return resultados;
        }

        @Override
        protected void onPostExecute(final List<Object> resultados) {
            ArrayList<Emprestimo> solicitados = (ArrayList<Emprestimo>) resultados.get(1);
            soliciteiAdapter = new EmprestimosAdapter(TelaAtividades.this, solicitados);
            listView.setAdapter(soliciteiAdapter);
            disponibilizados = (ArrayList<Emprestimo>) resultados.get(0);
            disponibilizeiAdapter = new EmprestimosAdapter(TelaAtividades.this, disponibilizados);

            pd.dismiss();
        }
    }

    private class SalvaEmprestimoAsync extends AsyncTask<Emprestimo, Void, Emprestimo> {
        Context context;
        private ProgressDialog pd;



        public SalvaEmprestimoAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            /*pd = new ProgressDialog(context);
            pd.setMessage("Atualizando solicitacoes..."); *//* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*//*
            pd.show();*/
        }


        protected Emprestimo doInBackground(Emprestimo... emprestimo) {
            /* São criadas Colection de avaliações e correções para armazenar o resultado das buscas n banco de dados.*/

            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */
            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null)
                        .setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service =  builder.build();

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
            Toast.makeText(getApplicationContext(),"Solicitacao de Emprestimo Salva",Toast.LENGTH_SHORT).show();
            /*pd.dismiss();*/

        }
    }
}
