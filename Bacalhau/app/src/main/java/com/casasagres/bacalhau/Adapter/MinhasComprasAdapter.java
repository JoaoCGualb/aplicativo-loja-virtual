package com.casasagres.bacalhau.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casasagres.bacalhau.Model.ItemPedido;
import com.casasagres.bacalhau.Model.Pedido;
import com.casasagres.bacalhau.R;

import java.text.DecimalFormat;
import java.util.List;

public class MinhasComprasAdapter extends RecyclerView.Adapter<MinhasComprasAdapter.MyViewHolder>{

    private List<Pedido> pedidos;
    private Context context;

    public MinhasComprasAdapter(List<Pedido> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;
    }

    @NonNull
    @Override
    public MinhasComprasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minhas_compras_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pedido pedido = pedidos.get(position);

        holder.data.setText(pedido.getData());
        holder.tipo.setText(pedido.getTipo());

        List<ItemPedido> itens = pedido.getItens();
        Double total = 0.0;
        for( ItemPedido itemPedido : itens ) {

            int qtde = itemPedido.getQuantidade();
            Double preco = itemPedido.getPreco();
            total += (qtde * preco);
        }

        DecimalFormat format = new DecimalFormat("0.00");
        String valorFinal = format.format(total);

        holder.valor.setText("Valor: R$ " +valorFinal);

        switch (pedido.getStatus()){
            case Pedido.PENDENTE:
                holder.status.setText("PEDIDO EM ABERTO");
                holder.status.setTextColor(context.getResources().getColor(R.color.aguardando));
                break;

            case Pedido.AGUARDANDO:
                holder.status.setText("AGUARDANDO CONFIRMAÇÃO DA LOJA");
                holder.status.setTextColor(context.getResources().getColor(R.color.aguardando));
                break;

            case Pedido.CONFIRMADO:
                holder.status.setText("PEDIDO CONFIRMADO");
                holder.status.setTextColor(context.getResources().getColor(R.color.confirmado));
                break;

            case Pedido.CANCELADO:
                holder.status.setText("CANCELADO");
                    holder.status.setTextColor(context.getResources().getColor(R.color.cancelado));
                break;

            case Pedido.A_CAMINHO:
                holder.status.setText("PEDIDO A CAMINHO");
                holder.status.setTextColor(context.getResources().getColor(R.color.a_caminho));
                break;

            case Pedido.FINALIZADO:
                holder.status.setText("PEDIDO FINALIZADO");
                holder.status.setTextColor(context.getResources().getColor(R.color.finalizado));
                break;
        }

        }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView data,status,valor,tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.textData);
            tipo = itemView.findViewById(R.id.textTipo);
            status = itemView.findViewById(R.id.textStatus);
            valor = itemView.findViewById(R.id.textValor);
        }
    }
}
