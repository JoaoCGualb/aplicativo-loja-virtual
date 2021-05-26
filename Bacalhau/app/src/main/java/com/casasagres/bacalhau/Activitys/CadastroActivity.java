package com.casasagres.bacalhau.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Model.Usuario;
import com.casasagres.bacalhau.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome,campoSenha,campoEmail,campoSobrenome;
    private Button botaoCadastrar;


    //firebase auth
    private FirebaseAuth autenticacao;

    //Classe do usuario
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoNome = findViewById(R.id.editCadastroNome);
        campoSobrenome = findViewById(R.id.editCadastroSobrenome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);

        getSupportActionBar().hide();

        botaoCadastrar = findViewById(R.id.buttonCadastrar);


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoSobrenome = campoSobrenome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                //Validar se campos foram preenchidos
                if(!textoNome.isEmpty()){
                    if (!textoSobrenome.isEmpty()) {
                        if (!textoEmail.isEmpty()) {
                            if (!textoSenha.isEmpty()) {

                                usuario = new Usuario();

                                usuario.setNome(textoNome);
                                usuario.setEmail(textoEmail);
                                usuario.setSobrenome(textoSobrenome);
                                usuario.setSenha(textoSenha);

                                cadastrarUsuario();


                            } else {
                                Toast.makeText(CadastroActivity.this,
                                        "Preencha o campo Senha!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha o campo Email!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o campo Nome!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    startActivity(new Intent(CadastroActivity.this,MainActivity.class));
                    finish();
                }else{

                    String excecao = "";
                    try{
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite uma email valido";
                    }catch (FirebaseAuthUserCollisionException e){
                        usuarioExistente();
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }



                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void usuarioExistente(){
        //Instanciar AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //Configurar Titulo e mensagem
        dialog.setTitle("Usuário já existente");
        dialog.setMessage("Este usuário ja esta cadastrado em nosso sistema, por favor realize o login ou se esqueceu" +
                "clique para redefinir senha");

        //Configurar cancelamento
        dialog.setCancelable(false);

        //Configurar icone
        dialog.setIcon(android.R.drawable.ic_btn_speak_now);

        //Configurar ações de Sim ou não
        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        dialog.setNegativeButton("Redefinir Senha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                autenticacao.sendPasswordResetEmail(campoEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Reset Password", "Email enviado.");
                                    Toast.makeText(CadastroActivity.this,
                                            "Email enviado com sucesso para o seguinte endereço: "
                                                    + campoEmail.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //Criar e exibir AlertDialog
        dialog.create();
        dialog.show();
    }
}
