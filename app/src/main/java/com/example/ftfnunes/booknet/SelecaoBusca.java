package com.example.ftfnunes.booknet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.bookNetBackend.BookNetBackend;
import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;
import com.appspot.myapplicationid.bookNetBackend.model.Usuario;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import org.w3c.dom.Text;

import de.greenrobot.event.EventBus;

public class SelecaoBusca extends AppCompatActivity {

    Anuncio anuncio;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_busca);

        anuncio = EventBus.getDefault().removeStickyEvent(Anuncio.class);
        usuario = EventBus.getDefault().getStickyEvent(Usuario.class);
        TextView tituloText = (TextView)findViewById(R.id.TituloText);
        TextView autorText = (TextView)findViewById(R.id.textAutor);
        TextView generoText = (TextView)findViewById(R.id.textGenero);
        TextView edText = (TextView)findViewById(R.id.textEdicao);
        TextView descText = (TextView)findViewById(R.id.textView6);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        Button solButton = (Button)findViewById(R.id.buttonSol);

        tituloText.setText(anuncio.getNomeDoLivro());
        autorText.setText(anuncio.getAutor());
        generoText.setText(anuncio.getGenero());
        edText.setText(anuncio.getEdicao()+" Edicao");
        descText.setText(anuncio.getDescricao());
        ratingBar.setRating(anuncio.getAnunciante().getAvaliacao());

        solButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setAnunciante(anuncio.getAnunciante());
                emprestimo.setInteressado(usuario);
                emprestimo.setAnuncio(anuncio);
                emprestimo.setStatus("Pendente");

                new SalvaEmprestimoAsync(SelecaoBusca.this).execute(emprestimo);
            }
        });

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
            pd.dismiss();

        }
    }
}
