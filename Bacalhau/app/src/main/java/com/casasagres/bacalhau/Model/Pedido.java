package com.casasagres.bacalhau.Model;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Helper.DataCustom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Pedido implements Serializable {

    private String idUsuario;
    private String tipo;
    private String idPedido;
    private String nome;
    private String endereco;
    private List<ItemPedido> itens;
    private Double total;
    private String status = PENDENTE;
    private int metodoPagamento;
    private String observacao;
    private String data;

    public static final String PENDENTE = "pendente";
    public static final String AGUARDANDO = "aguardando";
    public static final String CONFIRMADO = "confirmado";
    public static final String A_CAMINHO = "a_caminho";
    public static final String CANCELADO = "cancelado";
    public static final String FINALIZADO = "finalizado";


    public Pedido() {
    }

    public Pedido(String idUsu, String idEmp) {

        setIdUsuario( idUsu );
        setTipo( idEmp );
        setData(DataCustom.recuperarData());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child( idUsu )
                .child( idEmp );
        setIdPedido( pedidoRef.push().getKey() );

    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child( getIdUsuario() )
                .child( getTipo() )
                .child(getIdPedido());
        pedidoRef.setValue( this );

    }

    public void deletarPedido(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidosRef = firebaseRef
                .child("meus_pedidos")
                .child( getIdUsuario() )
                .child(getTipo())
                .child( getIdPedido() );

        pedidosRef.removeValue();

    }


    public void atualizarLista(){

        HashMap<String, Object> status = new HashMap<>();

        status.put("itens", getItens());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getTipo())
                .child(getIdPedido());
        pedidoRef.updateChildren(status);

    }

    public void confimar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child( getTipo() )
                .child( getIdPedido() );
        pedidoRef.setValue( this );

        atualizarMeusPedidos();

    }

    public void atualizarMeusPedidos() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());
        status.put("endereco", getEndereco());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("meus_pedidos")
                .child(getIdUsuario())
                .child(getTipo())
                .child(getIdPedido());
        pedidoRef.updateChildren(status);
    }

    public void atualizarStatusPedido(){

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus() );

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child( getTipo() )
                .child( getIdPedido() );
        pedidoRef.updateChildren( status );

        DatabaseReference meusPedidosRef = firebaseRef
                .child("meus_pedidos")
                .child( getIdUsuario() )
                .child( getTipo() )
                .child( getIdPedido() );
        meusPedidosRef.updateChildren( status );

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String idEmpresa) {
        this.tipo = idEmpresa;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

