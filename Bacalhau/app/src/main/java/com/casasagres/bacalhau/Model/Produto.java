package com.casasagres.bacalhau.Model;

import java.io.Serializable;

public class Produto implements Serializable {
    private String idProduto;
    private String titulo;
    private String descricao;
    private String tituloPesquisa;
    private String grupo;
    private Double preco;
    private String imagem;



    //Construtor
    public Produto() {
    }

    public Produto(String idProduto, String titulo, String descricao, String tituloPesquisa, String grupo) {
        this.idProduto = idProduto;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tituloPesquisa = tituloPesquisa;
        this.grupo = grupo;
    }

    //Get e SET
    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTituloPesquisa() {
        return tituloPesquisa;
    }

    public void setTituloPesquisa(String tituloPesquisa) {
        this.tituloPesquisa = tituloPesquisa;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    //Functions
}
