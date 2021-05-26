package com.casasagres.bacalhau.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casasagres.bacalhau.Activitys.MeusPedidosDetalheActivity;
import com.casasagres.bacalhau.Adapter.MinhasComprasAdapter;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Helper.RecyclerItemClickListener;
import com.casasagres.bacalhau.Model.Pedido;
import com.casasagres.bacalhau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MinhasComprasFragment extends Fragment {

    private RecyclerView recyclerMinhasCompras;
    private List<Pedido> pedidos = new ArrayList<>();
    private MinhasComprasAdapter adapter;

    private LinearLayout layoutLogado, layoutPublico;

    private DatabaseReference firebaseRef;
    private String idUsuario;

    public MinhasComprasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarPedidos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minhas_compras, container, false);



        idUsuario = UsuarioFirebase.getIndentificadorUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        adapter = new MinhasComprasAdapter(pedidos,getContext());

        layoutLogado = view.findViewById(R.id.layoutLogado);
        layoutPublico = view.findViewById(R.id.layoutPublico);

        recyclerMinhasCompras = view.findViewById(R.id.recyclerMinhasCompas);
        recyclerMinhasCompras.setHasFixedSize(true);
        recyclerMinhasCompras.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMinhasCompras.setAdapter(adapter);



        recyclerMinhasCompras.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerMinhasCompras,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Pedido pedidoSelecionado = pedidos.get(position);

                        Intent i = new Intent(getContext(), MeusPedidosDetalheActivity.class);
                        i.putExtra("pedidoSelecionado",pedidoSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        cancelarPedido(position);
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));

        verificarLogado();

        return view;
    }

    private void verificarLogado() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(auth.getCurrentUser() != null){
            layoutLogado.setVisibility(View.VISIBLE);
            layoutPublico.setVisibility(View.GONE);
        }else{
            layoutLogado.setVisibility(View.GONE);
            layoutPublico.setVisibility(View.VISIBLE);
        }

    }

    private void cancelarPedido(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Cancelar Pedido");
        builder.setMessage("Deseja realmente cancelar este Pedido");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Pedido pedido = pedidos.get(position);

                pedido.setStatus("cancelado");
                pedido.atualizarStatusPedido();
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

    private void recuperarPedidos() {
        final DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child(idUsuario);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.clear();

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.child("taberna").getChildren()) {
                        Pedido pedido =ds.getValue(Pedido.class);
                        if(!pedido.getStatus().equals("cancelado") || pedido.getStatus() != null){
                            pedidos.add(pedido);}
                    }
                    Collections.reverse(pedidos);

                    for (DataSnapshot ds : dataSnapshot.child("loja").getChildren()) {
                        Pedido pedido =ds.getValue(Pedido.class);
                        if(!pedido.getStatus().equals("cancelado")){
                            pedidos.add(pedido);}
                    }
                    Collections.reverse(pedidos);


                   /* Collections.sort(pedidos, new Comparator<Pedido>() {
                        @Override
                        public int compare(Pedido o1, Pedido o2) {
                            String data1 = o1.getData();
                            String data2 = o2.getData();
                            return data1.compareToIgnoreCase(data2);
                        }
                    });*/
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
