package com.casasagres.bacalhau.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.casasagres.bacalhau.API.CEPService;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.casasagres.bacalhau.Model.CEP;
import com.casasagres.bacalhau.Model.Endereco;
import com.casasagres.bacalhau.Model.Usuario;
import com.casasagres.bacalhau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdicionarEnderecoActivity extends AppCompatActivity {

    //Componentes
    private Retrofit retrofit;
    private MaskEditText editCep;
    private EditText editRua,editNumero,editBairro,editCidade;
    private Button buttonBusca,buttonSalvar;
    private ProgressBar progressCarregando;
    private Endereco enderecoRecuperado;
    private String idUsuario;
    private int posicao;
    private Usuario usuario;
    private List<Endereco> enderecos = new ArrayList<>();

    //Firebase
    private DatabaseReference firebaseRef;

    @Override
    protected void onStart() {
        super.onStart();
        //Recupera dados Usuario
        recuperarDadosUsuario();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_endereco);

        //Edita titulo da actionBar
        getSupportActionBar().setTitle("Adicionando Endereço");

        //Inicializa Componentes
        inicializarComponentes();

    }

    private void inicializarComponentes() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            enderecoRecuperado = (Endereco) bundle.getSerializable("enderecoEditar");
            posicao = bundle.getInt("posicao");
        }


        //Inicia componentes firebase
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        //Inicia componentes
        progressCarregando = findViewById(R.id.progressCarregando);
        editBairro = findViewById(R.id.editBairro);
        editCep = findViewById(R.id.editCep);
        editNumero = findViewById(R.id.editNumero);
        editCidade = findViewById(R.id.editCidade);
        editRua = findViewById(R.id.editRua);


        if(enderecoRecuperado != null){
            editBairro.setText(enderecoRecuperado.getBairro());
            editCidade.setText(enderecoRecuperado.getCidade());
            editNumero.setText(enderecoRecuperado.getNumero());
            editRua.setText(enderecoRecuperado.getRua());
        }


        buttonBusca = findViewById(R.id.buttonBusca);
        buttonSalvar = findViewById(R.id.buttonSalvarEndereco);

        //Criando evento de click de busca de CEP
        buttonBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validando dados
                if (!editCep.getText().toString().isEmpty()) {
                    if (editCep.isDone()) {

                        //exibindo progressbar
                        progressCarregando.setVisibility(View.VISIBLE);

                        //recuperando endereço
                        recuperarCEPRetrofit();
                    }else{
                        exibirMensagem("Cep deve conter 8 digitos!");
                    }

                }else{
                    exibirMensagem("Digite um Cep valido!");
                }



            }
        });

        //Criando evento de salvar endereço
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
            }
        });

    }

    private void salvarDados() {
        String rua = editRua.getText().toString();
        String numero = editNumero.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editCidade.getText().toString();

        //validando dados
        if(!rua.isEmpty()){
            if(!numero.isEmpty()){
                if(!bairro.isEmpty()){
                    if(!cidade.isEmpty()){

                        //Criando endereço
                        Endereco endereco = new Endereco();

                        endereco.setBairro(editBairro.getText().toString());
                        endereco.setCidade(editCidade.getText().toString());
                        endereco.setNumero(editNumero.getText().toString());
                        endereco.setRua(editRua.getText().toString());


                        if(enderecoRecuperado != null){
                            enderecos.remove(posicao);
                        }

                        //Adicionando endereço a lista de endereços usuario
                        enderecos.add(endereco);
                        usuario.setEnderecos(enderecos);

                        //Atualizando dados usuario
                        usuario.atualizar();

                        Toast.makeText(this, "Endereço salvo com sucesso", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{
                        exibirMensagem("Preencha o campo Cidade");
                    }
                }else{
                    exibirMensagem("Preencha o campo Bairro");
                }
            }else{
                exibirMensagem("Preencha o campo Número");
            }
        }else{
            exibirMensagem("Preencha o campo Rua");
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }



    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
    private void recuperarCEPRetrofit(){

        //Fazendo teclado sumir depois de iniciar a pesquisa
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        CEPService cepService = retrofit.create(CEPService.class);

        String cep = editCep.getText().toString();

        Call<CEP> call = cepService.recuperarCEP(cep);

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, retrofit2.Response<CEP> response) {
                if(response.isSuccessful() || response.body() != null){
                    CEP cep = response.body();

                    //Configurando campos com endereço encontrado
                    editRua.setText(cep.getLogradouro());
                    editBairro.setText(cep.getBairro());
                    editCidade.setText(cep.getLocalidade());


                }else{
                    exibirMensagem("Cep não Encontrado");
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(AdicionarEnderecoActivity.this, "CEP Invalido!", Toast.LENGTH_SHORT).show();
            }
        });

        progressCarregando.setVisibility(View.GONE);
    }

    private void recuperarDadosUsuario() {

        //Iniciando recuperação de dados do Usuario
        final DatabaseReference usuariosRef = firebaseRef
                .child("usuarios")
                .child( idUsuario );

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){
                    usuario = dataSnapshot.getValue(Usuario.class);

                    //Recuperando endereços ja cadastrados do usuario
                    enderecos = usuario.getEnderecos();

                    //Caso seja o primeiro endereço instancia lista
                    if(enderecos == null){
                        enderecos = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
