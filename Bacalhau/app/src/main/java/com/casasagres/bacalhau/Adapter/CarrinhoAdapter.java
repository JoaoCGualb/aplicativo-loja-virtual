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
import com.casasagres.bacalhau.Model.ItemPedido;
import com.casasagres.bacalhau.R;

import java.text.DecimalFormat;
import java.util.List;


public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {


    private List<ItemPedido> itemPedidos;
    private Context context;

    public CarrinhoAdapter(List<ItemPedido> listaProduto, Context context) {
        this.itemPedidos = listaProduto;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.carrinho_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemPedido item = itemPedidos.get(position);

        holder.titulo.setText(item.getNomeProduto());
        holder.quantidade.setText( "Qtd: " +String.valueOf(item.getQuantidade()));

        if(item.getImagemProduto() != null){
            Glide.with(context).load(item.getImagemProduto()).into(holder.imagem);
        }else {

            Glide.with(context).load(R.drawable.produto_model).into(holder.imagem);
        }

        Double precoTotal = item.getQuantidade() * item.getPreco();

        DecimalFormat format = new DecimalFormat("0.00");
        String valorFinal = format.format(precoTotal);

        holder.preco.setText("R$ " + valorFinal);

    }

    @Override
    public int getItemCount() {
        return itemPedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagem;
        TextView titulo,quantidade,preco;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imageProduto);
            titulo = itemView.findViewById(R.id.textTitulo);
            quantidade = itemView.findViewById(R.id.textQuantidade);
            preco = itemView.findViewById(R.id.textPreco);

        }
    }



}
