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

import de.greenrobot.event.EventBus;

public class TelaAtividades extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int APROVAR_SOLICITACAO = 1;
    Usuario usuario;
    EmprestimosAdapter soliciteiAdapter;
    EmprestimosAdapter disponibilizeiAdapter;
    ListView listView;

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

                            }
                            else{
                                Toast.makeText(TelaAtividades.this, "nofa, eh muito forte2", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                    listView.setAdapter(disponibilizeiAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Emprestimo emprestimo = (Emprestimo) parent.getItemAtPosition(position);
                            if(emprestimo.getStatus().equals("Pendente")) {
                                EventBus.getDefault().postSticky(emprestimo);
                                Intent intent = new Intent(TelaAtividades.this, AprovacaoDeSolicitacao.class);
                                TelaAtividades.this.startActivityForResult(intent, APROVAR_SOLICITACAO);
                            }
                            else{
                                Toast.makeText(TelaAtividades.this, "nofa, eh muito forte", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
        getMenuInflater().inflate(R.menu.tela_atividades, menu);
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
            ArrayList<Emprestimo> disponibilizados = (ArrayList<Emprestimo>) resultados.get(0);
            disponibilizeiAdapter = new EmprestimosAdapter(TelaAtividades.this, disponibilizados);

            pd.dismiss();
        }
    }
}
