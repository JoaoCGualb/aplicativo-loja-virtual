package com.casasagres.bacalhau.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.casasagres.bacalhau.Activitys.AdicionarEnderecoActivity;
import com.casasagres.bacalhau.Activitys.EditarPerfilActivity;
import com.casasagres.bacalhau.Adapter.EnderecosAdapter;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Helper.RecyclerItemClickListener;
import com.casasagres.bacalhau.Model.Endereco;
import com.casasagres.bacalhau.Model.Usuario;
import com.casasagres.bacalhau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView textNome, textTelefone, textEmail,textEditar;
    private ImageView imageAdicionarEndereco;


    private Usuario usuario;
    private String idUsuario;
    private List<Endereco> listaEnderecos = new ArrayList<>();
    private EnderecosAdapter adapter;
    private RecyclerView recyclerEnderecos;

    //Firebase
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioLogadoRef;
    private ValueEventListener valueEventListener;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuario();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view =inflater.inflate(R.layout.fragment_perfil, container, false);

        inicializarComponentes(view);

        textEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        imageAdicionarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdicionarEnderecoActivity.class));
            }
        });

        adapter = new EnderecosAdapter(listaEnderecos,getContext());
        recyclerEnderecos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerEnderecos.setHasFixedSize(true);
        recyclerEnderecos.setAdapter(adapter);

        recyclerEnderecos.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerEnderecos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        return view;
    }

    private void inicializarComponentes(View view){

        textEditar = view.findViewById(R.id.textEditarDados);
        textNome = view.findViewById(R.id.textNome);
        textTelefone = view.findViewById(R.id.textTelefone);
        textEmail = view.findViewById(R.id.textEmail);

        usuario = UsuarioFirebase.getDadosUsuarioLogado();
        usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");

        idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        imageAdicionarEndereco = view.findViewById(R.id.imageAdicionarEndereco);

        recyclerEnderecos = view.findViewById(R.id.recyclerEnderecos);

        recyclerEnderecos.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerEnderecos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final Endereco endereco = listaEnderecos.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Endereço: ");
                builder.setMessage("Deseja Deletar ou Editar o endereço :\n "+
                            endereco.getRua()  +", " + endereco.getNumero()
                        );
                builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listaEnderecos.remove(position);
                        usuario.setEnderecos(listaEnderecos);
                        usuario.atualizar();
                        adapter.notifyItemRemoved(position);
                    }
                });

                builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getContext(),AdicionarEnderecoActivity.class);
                        i.putExtra("enderecoEditar", endereco);
                        i.putExtra("posicao", position);
                        startActivity(i);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }

    private void recuperarDadosUsuario(){

        usuarioLogadoRef = usuarioRef.child(idUsuario);

        valueEventListener = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaEnderecos.clear();


                usuario = snapshot.getValue(Usuario.class);
                String nome = usuario.getNome() + " " + usuario.getSobrenome();
                textNome.setText("Nome: " + nome);
                textEmail.setText("Email: " + usuario.getEmail());
                textTelefone.setText("Tel: " + usuario.getTelefone());

                for(DataSnapshot dados: snapshot.child("enderecos").getChildren()){
                    Endereco endereco = dados.getValue(Endereco.class);
                    listaEnderecos.add(endereco);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}