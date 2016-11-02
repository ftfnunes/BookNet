package com.example.ftfnunes.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;

import de.greenrobot.event.EventBus;

public class StatusEmprestimo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Emprestimo emprestimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_emprestimo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emprestimo = EventBus.getDefault().removeStickyEvent(Emprestimo.class);

        RatingBar rb = (RatingBar)findViewById(R.id.ratingBar2);
        TextView tituloText = (TextView) findViewById(R.id.TituloText);
        TextView autorText = (TextView) findViewById(R.id.textAutor);
        TextView edText = (TextView) findViewById(R.id.textGenero);
        TextView userText = (TextView) findViewById(R.id.textNomeUsuario);
        TextView telText = (TextView) findViewById(R.id.textTelefone);
        TextView statusText = (TextView)findViewById(R.id.textStatus);

        rb.setRating(emprestimo.getAnunciante().getAvaliacao());
        statusText.setText(emprestimo.getStatus());
        tituloText.setText(emprestimo.getAnuncio().getNomeDoLivro());
        autorText.setText(emprestimo.getAnuncio().getAutor());
        edText.setText(emprestimo.getAnuncio().getEdicao() + " Edicao");
        userText.setText(emprestimo.getInteressado().getUserName());
        telText.setText("3212344");

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
        getMenuInflater().inflate(R.menu.status_emprestimo, menu);
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
}
