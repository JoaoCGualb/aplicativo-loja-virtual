package com.casasagres.bacalhau.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Model.Usuario;
import com.casasagres.bacalhau.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail,campoSenha;
    private Button botaoLogin;

    private Usuario usuario;

    //Firebase
    private FirebaseAuth autenticacao;


    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);


        botaoLogin = findViewById(R.id.buttonLogin);


        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){

                        usuario = new Usuario();

                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarUsuario();


                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha o campo Senha!",
                                Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Preencha o campo Email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            abrirTelaPrincipal();
                        } else {

                            String excecao = "";
                            try{
                                throw  task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não esta cadastrado";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }



                            Toast.makeText(LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    public void abrirTelaCadastro(View view){
        startActivity(new Intent(LoginActivity.this,CadastroActivity.class));
        finish();
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }
}
