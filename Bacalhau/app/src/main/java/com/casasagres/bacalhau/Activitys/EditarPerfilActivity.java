package com.casasagres.bacalhau.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Model.Usuario;
import com.casasagres.bacalhau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText campoNome,campoSobrenome,campoEmail;
    private MaskEditText campoTelefone;
    private FloatingActionButton fabSalvar;

    private Usuario usuario;

    //Firebase
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioLogadoRef;
    private ValueEventListener valueEventListener;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        campoNome = findViewById(R.id.editPerfilNome);
        campoSobrenome =findViewById(R.id.editPerfilSobrenome);
        campoEmail = findViewById(R.id.editPerfilEmail);
        campoTelefone = findViewById(R.id.editPerfilTelefone);
        fabSalvar = findViewById(R.id.fabSalvarPerfil);

        //Inicializando firebase
        usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        usuario = UsuarioFirebase.getDadosUsuarioLogado();

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               salvarDados();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return super.onSupportNavigateUp();
    }
    private void salvarDados() {
        String nome = campoNome.getText().toString();
        String sobrenome = campoSobrenome.getText().toString();
        String telefone = campoTelefone.getText().toString();

        if(!nome.isEmpty()){
            if(!sobrenome.isEmpty()){
                if(!telefone.isEmpty()){
                    usuario.setNome(campoNome.getText().toString());
                    usuario.setSobrenome(campoSobrenome.getText().toString());
                    usuario.setTelefone( campoTelefone.getText().toString() );
                    usuario.atualizar();
                    finish();
                }else{
                    exibirMensagem("Preencha o campo Telefone");
                }
            }else{
                exibirMensagem("Preencha o campo Sobrenome");
            }
        }else{
            exibirMensagem("Preencha o campo Nome");
        }


    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void recuperarDadosUsuario(){

        usuarioLogadoRef = usuarioRef.child(usuario.getId());

        valueEventListener = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);

                campoNome.setText(usuario.getNome());
                campoEmail.setText(usuario.getEmail());
                campoSobrenome.setText(usuario.getSobrenome());
                campoTelefone.setText(usuario.getTelefone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
