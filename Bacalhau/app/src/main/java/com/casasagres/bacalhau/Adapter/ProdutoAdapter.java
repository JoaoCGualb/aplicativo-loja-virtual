package com.casasagres.bacalhau.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.casasagres.bacalhau.Model.Produto;
import com.casasagres.bacalhau.R;

import java.text.DecimalFormat;
import java.util.List;


public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> {

    private List<Produto> listaProduto;
    private Context context;

    public ProdutoAdapter(List<Produto> listaProduto, Context context) {
        this.listaProduto = listaProduto;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.produtos_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = listaProduto.get(position);

        holder.titulo.setText(produto.getTitulo());
        holder.descricao.setText(produto.getDescricao());

        DecimalFormat format = new DecimalFormat("0.00");
        String valorFinal = format.format(produto.getPreco());

        holder.preco.setText("R$ " + valorFinal);

        if(produto.getImagem() != null){
            Glide.with(context).load(produto.getImagem()).into(holder.imagem);
        }else {

            Glide.with(context).load(R.drawable.produto_model).into(holder.imagem);
        }

    }

    @Override
    public int getItemCount() {
        return listaProduto.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagem;
        TextView titulo,descricao,preco;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           imagem = itemView.findViewById(R.id.imageProduto);
           titulo = itemView.findViewById(R.id.textTitulo);
           descricao = itemView.findViewById(R.id.textDescricao);
           preco = itemView.findViewById(R.id.textPreco);

        }
    }



}
