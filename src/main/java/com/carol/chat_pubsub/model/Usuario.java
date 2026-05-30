package com.carol.chat_pubsub.model;

public class Usuario {
    private String nome;
    private String senha;
    private String chavePublica;
    
    public Usuario (String nome) {
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
}
