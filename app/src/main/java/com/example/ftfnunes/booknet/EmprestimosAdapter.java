package com.example.ftfnunes.booknet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.myapplicationid.bookNetBackend.model.Anuncio;
import com.appspot.myapplicationid.bookNetBackend.model.Emprestimo;

import java.util.List;

/**
 * Created by ftfnunes on 31/10/16.
 */

public class EmprestimosAdapter extends ArrayAdapter<Emprestimo> {

    protected Context context;
    protected List<Emprestimo> emprestimos = null;

    public EmprestimosAdapter(Context context, List<Emprestimo> emprestimos) {
        super(context,0, emprestimos);
        this.emprestimos = emprestimos;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Emprestimo emprestimo = emprestimos.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_layout, null);

        TextView textViewTitulo = (TextView) view.findViewById(R.id.textTituloList);
        textViewTitulo.setText(emprestimo.getAnuncio().getNomeDoLivro());

        TextView textViewStatus = (TextView)view.findViewById(R.id.textAutorList);
        textViewStatus.setText(emprestimo.getStatus());
        return view;
    }
}
