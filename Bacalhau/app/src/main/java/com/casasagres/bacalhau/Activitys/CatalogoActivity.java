package com.casasagres.bacalhau.Activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.casasagres.bacalhau.Adapter.ProdutoAdapter;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Helper.RecyclerItemClickListener;
import com.casasagres.bacalhau.Model.ItemPedido;
import com.casasagres.bacalhau.Model.Pedido;
import com.casasagres.bacalhau.Model.Produto;
import com.casasagres.bacalhau.Model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casasagres.bacalhau.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CatalogoActivity extends AppCompatActivity {

    private FloatingActionButton fabCarrinho;
    private RecyclerView recyclerCatalogo;
    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos = new ArrayList<>();
    private String tipoCatalogo;
    private String idUsuario;
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private Pedido pedidoRecuperado;
    private int quantidade;
    private Produto produtoSelecionado;
    private Usuario usuario;

    private ValueEventListener valueEventListenerProdutos;
    private DatabaseReference produtosRef;
    private FirebaseUser usuarioLogado;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        inicializarComponentes();

        configurandoRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarProdutos();
        recuperarDadosUsuario();
        dialogCarregamento.dismiss();
    }


    @Override
    protected void onStop() {
        super.onStop();
        produtosRef.removeEventListener(valueEventListenerProdutos);
    }

    @SuppressLint("RestrictedApi")
    private void inicializarComponentes() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Catálogo");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabCarrinho = findViewById(R.id.fabCarrinho);

        produtosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("produtos");
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        recyclerCatalogo = findViewById(R.id.recyclerCatalogo);

        usuarioLogado = UsuarioFirebase.getUsuarioAtual();

        idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        fabCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(CatalogoActivity.this,CarrinhoActivity.class);
                i.putExtra("idPedido",pedidoRecuperado.getIdPedido());
                i.putExtra("tipoCatalogo", tipoCatalogo);
                startActivity(i);
            }
        });


        recyclerCatalogo.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerCatalogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(usuarioLogado!=null) {
                    produtoSelecionado = listaProdutos.get(position);

                    alertProdutos();
                }else{
                    usuarioNaoLogado();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));

    }

    private void configurandoRecyclerView() {

        adapter = new ProdutoAdapter(listaProdutos,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CatalogoActivity.this);

        recyclerCatalogo.setHasFixedSize(true);
        recyclerCatalogo.setLayoutManager(layoutManager);
        recyclerCatalogo.setAdapter(adapter);
    }

    private void recuperarDadosUsuario() {


        dialogCarregamento = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable( false )
                .build();
        dialogCarregamento.show();

        DatabaseReference usuariosRef = firebaseRef
                .child("usuarios")
                .child( idUsuario );

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }


                recuperPedido();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void recuperPedido() {

        DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child( idUsuario )
                .child( tipoCatalogo );

        Query pedidoAberto = pedidoRef.orderByChild("status").equalTo("pendente");

        pedidoAberto.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itensCarrinho = new ArrayList<>();
                itensCarrinho.clear();
                if(dataSnapshot.getValue() != null){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Pedido pedido = ds.getValue(Pedido.class);

                        if(pedido.getStatus().equals("pendente")) {
                            pedidoRecuperado = pedido;
                            itensCarrinho = pedidoRecuperado.getItens();
                            if (itensCarrinho.size() > 0 || itensCarrinho != null) {
                                fabCarrinho.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void recuperarProdutos(){
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            tipoCatalogo = bundle.getString("tipoCatalogo");
        }

        listaProdutos.clear();

        DatabaseReference catalogoRef = produtosRef.child(tipoCatalogo);

        valueEventListenerProdutos = catalogoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    listaProdutos.add(produto);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return super.onSupportNavigateUp();
    }


    // Alert Dialog

    private android.app.AlertDialog dialogCarregamento;
    private AlertDialog dialog;
    private TextView textTitulo,textDescricao,textPreco,textQuantidade;
    private FloatingActionButton fabMenos,fabMais;
    private ImageView imageProduto;
    private Button buttonAdicionar;


    private void inicializarAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View layout = View.inflate(this,R.layout.alert_produto,null);


        textTitulo = layout.findViewById(R.id.textTituloProduto);
        textDescricao =  layout.findViewById(R.id.textDescricaoProduto);
        textPreco =layout.findViewById(R.id.textPrecoProduto);
        textQuantidade =layout.findViewById(R.id.textQuantidade);

        imageProduto = layout.findViewById(R.id.imageProduto);

        fabMais = layout.findViewById(R.id.fabMais);
        fabMenos = layout.findViewById(R.id.fabMenos);

        buttonAdicionar = layout.findViewById(R.id.buttonAdicionar);



        builder.setView(layout);


        dialog = builder.create();
    }


    private void alertProdutos(){

        inicializarAlertDialog();

        textTitulo.setText(produtoSelecionado.getTitulo());
        textDescricao.setText(produtoSelecionado.getDescricao());

        DecimalFormat format = new DecimalFormat("0.00");
        String valorFinal = format.format(produtoSelecionado.getPreco());

        textPreco.setText( "R$ " + valorFinal);

        textQuantidade.setText("1");

        if(produtoSelecionado.getImagem() != null){
            Glide.with(this).load(produtoSelecionado.getImagem()).into(imageProduto);
        }else {

            Glide.with(this).load(R.drawable.produto_model).into(imageProduto);
        }



        fabMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade = Integer.parseInt(textQuantidade.getText().toString());

                quantidade -= 1;

                if(quantidade<1){
                    quantidade = 1;
                }

                textQuantidade.setText(String.valueOf(quantidade));

            }
        });

        fabMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade = Integer.parseInt(textQuantidade.getText().toString());

                quantidade += 1;

                textQuantidade.setText(String.valueOf(quantidade));

            }
        });

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                quantidade = Integer.parseInt(textQuantidade.getText().toString());

                if(quantidade != 0) {
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
                    itemPedido.setNomeProduto(produtoSelecionado.getTitulo());
                    itemPedido.setPreco(produtoSelecionado.getPreco());
                    itemPedido.setImagemProduto(produtoSelecionado.getImagem());
                    itemPedido.setQuantidade(quantidade);


                    for(ItemPedido item : itensCarrinho){
                        if(item.getIdProduto().equals(itemPedido.getIdProduto())){
                            itemPedido.setQuantidade( item.getQuantidade() + itemPedido.getQuantidade());
                            itensCarrinho.remove(item);
                        }
                    }

                    itensCarrinho.add(itemPedido);

                    if (pedidoRecuperado == null) {
                        pedidoRecuperado = new Pedido(idUsuario, tipoCatalogo);
                    }

                    pedidoRecuperado.setNome(usuario.getNome());
                    pedidoRecuperado.setItens(itensCarrinho);
                    pedidoRecuperado.salvar();

                    if (itensCarrinho.size() > 0) {
                        fabCarrinho.setVisibility(View.VISIBLE);
                    }
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }



    private void usuarioNaoLogado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Usuario não Logado");
        builder.setMessage("Esta área é destinada a usuarios previamente logados, para acessa-la pedimos que realize o login primeiramente." +
                "\nAgradeçemos a compreenção.");

        builder.setPositiveButton("Logar-se", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirTelaLogin();
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

    private void abrirTelaLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }



}
