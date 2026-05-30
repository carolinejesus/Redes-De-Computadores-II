package com.carol.chat_pubsub.model;

public class Mensagem {
    private String topico;
    private String remetente;
    private String conteudo;
    private long timestamp;
    private String assinatura;
    
    public Mensagem(String topico, String remetente, String conteudo) {
        this.topico = topico;
        this.remetente = remetente;
        this.conteudo = conteudo;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTopico() {
        return topico;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getConteudo() {
        return conteudo;
    }
}
