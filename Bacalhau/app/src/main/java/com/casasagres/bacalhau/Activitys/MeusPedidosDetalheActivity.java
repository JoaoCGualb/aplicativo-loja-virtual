package com.casasagres.bacalhau.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.casasagres.bacalhau.Adapter.CarrinhoAdapter;
import com.casasagres.bacalhau.Model.ItemPedido;
import com.casasagres.bacalhau.Model.Pedido;
import com.casasagres.bacalhau.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeusPedidosDetalheActivity extends AppCompatActivity {

    private Pedido pedidoSelecionado;
    private CarrinhoAdapter adapter;
    private RecyclerView recyclerItens;
    private List<ItemPedido> itensPedido = new ArrayList<>();
    private TextView textTotal, textData, textEndereço,textStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pedidos_detalhe);

        getSupportActionBar().setTitle("Detalhes do Pedido");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();

        textData.setText(pedidoSelecionado.getData());
        textEndereço.setText(pedidoSelecionado.getEndereco());

        calcularTotal();

        switch (pedidoSelecionado.getStatus()){
            case Pedido.PENDENTE:
                textStatus.setText("PEDIDO EM ABERTO");
                textStatus.setTextColor(getResources().getColor(R.color.aguardando));
                break;

            case Pedido.AGUARDANDO:
                textStatus.setText("AGUARDANDO CONFIRMAÇÃO DA LOJA");
                textStatus.setTextColor(getResources().getColor(R.color.aguardando));
                break;

            case Pedido.CONFIRMADO:
                textStatus.setText("PEDIDO CONFIRMADO");
                textStatus.setTextColor(getResources().getColor(R.color.confirmado));
                break;

            case Pedido.CANCELADO:
                textStatus.setText("CANCELADO");
                textStatus.setTextColor(getResources().getColor(R.color.cancelado));
                break;

            case Pedido.A_CAMINHO:
                textStatus.setText("PEDIDO A CAMINHO");
                textStatus.setTextColor(getResources().getColor(R.color.a_caminho));
                break;

            case Pedido.FINALIZADO:
                textStatus.setText("PEDIDO FINALIZADO");
                textStatus.setTextColor(getResources().getColor(R.color.finalizado));
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void calcularTotal() {
        Double totalPedido = Double.valueOf(0);

        for(ItemPedido item : itensPedido){
            Double preco = item.getPreco();
            int quant = item.getQuantidade();

            Double total = preco*quant;

            totalPedido += total;

        }
        //Double valorTotal = totalPedido + 5;
        Double valorTotal = totalPedido;

        DecimalFormat format = new DecimalFormat("0.00");
        String valorFinal = format.format(valorTotal);

        textTotal.setText("R$ " + valorFinal);
    }

    private void inicializarComponentes() {

        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            pedidoSelecionado = (Pedido) bundle.getSerializable("pedidoSelecionado");
        }


        textTotal = findViewById(R.id.textTotal);
        textData = findViewById(R.id.textData);
        textEndereço = findViewById(R.id.textEndereco);
        textStatus = findViewById(R.id.textStatus);
        itensPedido = pedidoSelecionado.getItens();

        adapter = new CarrinhoAdapter(itensPedido,this);

        recyclerItens = findViewById(R.id.recyclerPedido);
        recyclerItens.setHasFixedSize(true);
        recyclerItens.setLayoutManager(new LinearLayoutManager(this));
        recyclerItens.setAdapter(adapter);

    }


    public void cancelarPedido(View view){
        pedidoSelecionado.setStatus("cancelado");
        pedidoSelecionado.atualizarStatusPedido();
        Toast.makeText(this, "Pedido Cancelado com Sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }
}
