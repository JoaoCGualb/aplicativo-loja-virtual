package com.casasagres.bacalhau.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.casasagres.bacalhau.Adapter.CarrinhoAdapter;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Helper.RecyclerItemClickListener;
import com.casasagres.bacalhau.Model.Endereco;
import com.casasagres.bacalhau.Model.ItemPedido;
import com.casasagres.bacalhau.Model.Pedido;
import com.casasagres.bacalhau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class CarrinhoActivity extends AppCompatActivity {

    private Bundle bundle;
    private String idPedido;
    private String idUsuario;
    private String tipoCatalogo;

    private TextView textTotal,textTotalCarrinho,textTaxa;
    private int metodoPagamento= -1;

    private CarrinhoAdapter adapter;
    private RecyclerView recyclerCarrinho;
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private Pedido pedidoRecuperado;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuarioLogadoRef;
    private List<String> listaEnderecos = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        recuperarEnderecos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        getSupportActionBar().setTitle("Meu Carrinho");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();


        recuperPedido();
        recyclerCarrinho.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerCarrinho,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        cancelarPedido(position);
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));
    }

    private void inicializarComponentes() {
        bundle = getIntent().getExtras();

        if(bundle != null){
            idPedido = bundle.getString("idPedido");
            tipoCatalogo = bundle.getString("tipoCatalogo");
        }

        textTotal = findViewById(R.id.textTotal);
        //textTotalCarrinho = findViewById(R.id.textPrecoCarrinho);
        //textTaxa = findViewById(R.id.textTaxa);

        idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        recyclerCarrinho = findViewById(R.id.recyclerCarrinho);
    }

    private void configurandoRecyclerView() {

        adapter = new CarrinhoAdapter(itensCarrinho,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CarrinhoActivity.this);

        recyclerCarrinho.setHasFixedSize(true);
        recyclerCarrinho.setLayoutManager(layoutManager);
        recyclerCarrinho.setAdapter(adapter);
    }

    private void recuperPedido() {
        DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child( idUsuario )
                .child( tipoCatalogo )
                .child(idPedido);

        pedidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itensCarrinho = new ArrayList<>();
                if(dataSnapshot.getValue() != null){
                        Pedido pedido = dataSnapshot.getValue(Pedido.class);
                        pedidoRecuperado = pedido;
                        itensCarrinho = pedidoRecuperado.getItens();
                }
                atualizarLayout();
                configurandoRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void atualizarLayout(){

        Double totalCarrinho = Double.valueOf(0);
        DecimalFormat format = new DecimalFormat("0.00");
        for(ItemPedido item : itensCarrinho){
            Double preco = item.getPreco();
            int quant = item.getQuantidade();

            Double total = preco*quant;

            totalCarrinho += total;
        }
        /*

        String valorCarrinho = format.format(totalCarrinho);

        textTotalCarrinho.setText("R$ " + valorCarrinho );

        textTaxa.setText("R$ 5,00");



        Double valorTotal = totalCarrinho + 5;
         */
        Double valorTotal = totalCarrinho;


        String valorFinal = format.format(valorTotal);

        textTotal.setText("R$ " + valorFinal);


    }

    //AlertDialog

    private RadioGroup pagamento;
    private Spinner endereco;
    private EditText editObservacao;
    private Button buttonFinalizar;




    public void confirmarPedido(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(CarrinhoActivity.this);

        View layout = View.inflate(getApplicationContext(),R.layout.alert_finalizando,null);
        builder.setView(layout);

        editObservacao = layout.findViewById(R.id.editObservacao);
        endereco = layout.findViewById(R.id.spinnerEndereco);
        pagamento = layout.findViewById(R.id.grupPagamento);
        buttonFinalizar = layout.findViewById(R.id.buttonFinalizar);


        pagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case R.id.radioDinheiro:
                        metodoPagamento = 0;
                        break;
                    case R.id.radioDebito:
                        metodoPagamento = 1;
                        break;
                    case R.id.radioCredito:
                        metodoPagamento = 2;
                        break;
                }
            }
        });

        ArrayAdapter<String> adapterEnderecos = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listaEnderecos);
        adapterEnderecos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endereco.setAdapter(adapterEnderecos);


        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enderecoSelecionado = endereco.getSelectedItem().toString();

                if(metodoPagamento != -1){
                    if(!enderecoSelecionado.isEmpty()){
                        String observacao = editObservacao.getText().toString();
                        pedidoRecuperado.setMetodoPagamento( metodoPagamento );
                        pedidoRecuperado.setObservacao( observacao );
                        pedidoRecuperado.setEndereco(enderecoSelecionado);
                        pedidoRecuperado.setStatus(Pedido.AGUARDANDO);
                        pedidoRecuperado.confimar();
                        pedidoRecuperado = null;

                        Toast.makeText(CarrinhoActivity.this, "Pedido Confirmado com Sucesso", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CarrinhoActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(CarrinhoActivity.this, "Selecione um endereço", Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    Toast.makeText(CarrinhoActivity.this, "Selecione o metodo de Pagamento", Toast.LENGTH_SHORT).show();
                }


                
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void recuperarEnderecos() {

        usuarioLogadoRef = firebaseRef.child("usuarios").child(idUsuario).child("enderecos");

            usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dados: snapshot.getChildren()){
                        Endereco endereco = dados.getValue(Endereco.class);

                        String enderecoString = endereco.getRua() + ", " + endereco.getNumero();

                        listaEnderecos.add(enderecoString);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void cancelarPedido(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Deletar Item");
        builder.setMessage("Deseja realmente Deletar esse Item? ");
        builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                itensCarrinho.remove(position);
                pedidoRecuperado.setItens(itensCarrinho);
                pedidoRecuperado.deletarPedido();
                adapter.notifyItemRemoved(position);
                atualizarLayout();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }
}
