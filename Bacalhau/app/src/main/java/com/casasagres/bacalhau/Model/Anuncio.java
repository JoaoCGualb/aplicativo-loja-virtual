package com.casasagres.bacalhau.Model;

public class Anuncio {

    private String titulo;
    private String descricao;
    private String imagem;
    private boolean carrosel;


    //Construtor
    public Anuncio() {
    }

    public Anuncio(String titulo, String descricao, String imagem) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    //GET e SET


    public boolean isCarrosel() {
        return carrosel;
    }

    public void setCarrosel(boolean carrosel) {
        this.carrosel = carrosel;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    //Funcoes

}
