package com.example.ftfnunes.booknet;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.AnuncioCollection;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class TelaBusca extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String selectedFilter = "Titulo";
    ListView anunciosView;
    EditText searchText;
    Usuario usuario;
    public static int GET_FILTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_busca2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText = (EditText)findViewById(R.id.editText);
        anunciosView = (ListView)findViewById(R.id.anunciosList);

        usuario = EventBus.getDefault().getStickyEvent(Usuario.class);

        ImageButton searchButton = (ImageButton)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Codigo para procura no banco e preenchimento da listview vem aqui*/
                new RecuperaAnunciosAsync(TelaBusca.this).execute(searchText.getText().toString());
            }
        });

        ImageButton filterButton = (ImageButton)findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(TelaBusca.this, TelaFiltro.class);
                startActivityForResult(filterIntent, GET_FILTER);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            selectedFilter = data.getStringExtra("SELECTED_FILTER");
        }
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
        getMenuInflater().inflate(R.menu.tela_busca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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
    public boolean onNavigationItemSelected (MenuItem item){
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
    private class RecuperaAnunciosAsync extends AsyncTask<String, Void, List<Anuncio>> {
        Context context;
        private ProgressDialog pd;


        public RecuperaAnunciosAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Buscando Avaliacões..."); /* Tal mensagem é exibida enquanto a busca no banco de dados é realizada.*/
            pd.show();
        }


        protected List<Anuncio> doInBackground(String... filtro) {
            /* São criadas Colection de avaliações e correções para armazenar o resultado das buscas n banco de dados.*/
            AnuncioCollection resultadoAnuncios = new AnuncioCollection();
            /* Uma lista de objetos é criada para aramazenar os dois conjuntos de avaliações e correções. */

            try{
                BookNetBackend.Builder builder = new BookNetBackend.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
                builder.setRootUrl("https://booknet-148017.appspot.com/_ah/api/");
                BookNetBackend service =  builder.build();
                switch (TelaBusca.this.selectedFilter){
                    case "Titulo":
                        resultadoAnuncios = service.listPorTitulo(filtro[0] != null ? filtro[0] : "").execute();
                        break;
                    case "Autor":
                        resultadoAnuncios = service.listPorAutor(filtro[0] != null ? filtro[0] : "").execute();
                        break;
                    case "Genero":
                        resultadoAnuncios = service.listPorGenero(filtro[0] != null ? filtro[0] : "").execute();
                        break;
                    default:
                        resultadoAnuncios = service.listPorTitulo(filtro[0] != null ? filtro[0] : "").execute();
                        break;
                }
            }
            catch(Exception ex){
                /* Caso a busca de errado, uma mensagem de erro é exibida na tela do dispositivo.*/
                Log.d("Erro ao salvar", ex.getMessage(), ex);
            }
            return resultadoAnuncios.getItems();
        }


        @Override
        protected void onPostExecute(final List<Anuncio> anuncios) {
            if(anuncios != null) {
                ArrayList<Anuncio> filteredList = new ArrayList<>();
                for(Anuncio anuncio : anuncios){
                    if(!anuncio.getAnunciante().getUserName().equals(usuario.getUserName())){
                        filteredList.add(anuncio);
                    }
                }
                if(filteredList.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nenhum Resultado Encontrado",Toast.LENGTH_SHORT).show();
                }
                AnunciosAdpter adapter = new AnunciosAdpter(TelaBusca.this, filteredList);
                TelaBusca.this.anunciosView.setAdapter(adapter);
            }
            else
                Toast.makeText(getApplicationContext(),"Nenhum Resultado Encontrado",Toast.LENGTH_SHORT).show();

            pd.dismiss(); }
    }
}

class AnunciosAdpter extends ArrayAdapter<Anuncio> {

    private Context context;
    private List<Anuncio> anuncios = null;
    private Anuncio anuncioSelecionado;

    public AnunciosAdpter(Context context, List<Anuncio> anuncios) {
        super(context,0, anuncios);
        this.anuncios = anuncios;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        anuncioSelecionado = anuncios.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_layout, null);
        TextView textViewNomeZombie = (TextView) view.findViewById(R.id.textTituloList);
        textViewNomeZombie.setText(anuncioSelecionado.getNomeDoLivro());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelecaoBusca.class);
                EventBus.getDefault().postSticky(anuncioSelecionado);
                context.startActivity(intent);
            }
        });
        TextView textViewIdade = (TextView)view.findViewById(R.id.textAutorList);
        textViewIdade.setText(anuncioSelecionado.getAutor());
        return view;
    }
}


