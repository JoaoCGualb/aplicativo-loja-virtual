package com.casasagres.bacalhau.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casasagres.bacalhau.Model.Anuncio;
import com.casasagres.bacalhau.R;

import java.util.List;


public class AnunciosAdapter extends RecyclerView.Adapter <AnunciosAdapter.MyViewHolder> {

    List<Anuncio> listaAnuncios;
    Context context;

    public AnunciosAdapter(List<Anuncio> listaAnuncios, Context context) {
        this.listaAnuncios = listaAnuncios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.anuncio_adapter,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

         Anuncio anuncio = listaAnuncios.get(position);

         holder.titulo.setText(anuncio.getTitulo());
         holder.descricao.setText(anuncio.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaAnuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titulo,descricao;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textNoticiaTitulo);
            descricao = itemView.findViewById(R.id.textNoticiaDescricao);

        }
    }
}
