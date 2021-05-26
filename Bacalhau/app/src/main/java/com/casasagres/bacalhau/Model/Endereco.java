package com.casasagres.bacalhau.Model;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Configuracao.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Endereco implements Serializable {

    private String rua;
    private String bairro;
    private String numero;
    private String cidade;

    public Endereco() {
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    public void deletarPedido(){

        String idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidosRef = firebaseRef
                .child("usuarios")
                .child( idUsuario )
                .child("enderecos")
                .child( getRua() );

        pedidosRef.removeValue();

    }

    public void salvar(){

        String idUsuario = UsuarioFirebase.getIndentificadorUsuario();

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(idUsuario)
                .child("enderecos")
                .setValue(this);

    }
}
