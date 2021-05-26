package com.casasagres.bacalhau.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.casasagres.bacalhau.Model.Endereco;
import com.casasagres.bacalhau.R;

import java.util.List;

public class EnderecosAdapter extends RecyclerView.Adapter <EnderecosAdapter.MyViewHolder> {

    List<Endereco> listaEnderecos;
    Context context;

    public EnderecosAdapter(List<Endereco> listaEnderecos, Context context) {
        this.listaEnderecos = listaEnderecos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.enderecos_adapter,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Endereco endereco = listaEnderecos.get(position);

        String rua  = endereco.getRua();
        String numero  = endereco.getNumero();
        String bairro  = endereco.getBairro();
        String cidade  = endereco.getCidade();


        String ruaNumero = rua + ", " + numero;
        String bairroCidade = bairro + ", " + cidade;

        holder.rua.setText(ruaNumero);
        holder.bairro.setText(bairroCidade);

    }

    @Override
    public int getItemCount() {
        return listaEnderecos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView rua,bairro;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rua = itemView.findViewById(R.id.textRua);
            bairro = itemView.findViewById(R.id.textBairro);

        }
    }
}
