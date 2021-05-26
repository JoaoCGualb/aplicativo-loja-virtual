package com.casasagres.bacalhau.Model;


import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private List<Endereco> enderecos;
    private String telefone;


    //Contrutores
    public Usuario() {
    }

    //GET e SET

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    //Funções

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(getId())
                .setValue(this);
    }

    public void atualizar(){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        String idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        Map objeto = new HashMap();
        objeto.put("/usuarios/" + idUsuario + "/nome",getNome());
        objeto.put("/usuarios/" + idUsuario + "/sobrenome",getSobrenome());
        objeto.put("/usuarios/" + idUsuario + "/email",getEmail());
        objeto.put("/usuarios/" + idUsuario + "/enderecos",getEnderecos());
        objeto.put("/usuarios/" + idUsuario + "/telefone",getTelefone());

        database.updateChildren(objeto);
    }

}

