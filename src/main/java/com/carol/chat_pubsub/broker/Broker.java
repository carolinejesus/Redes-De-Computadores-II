package com.carol.chat_pubsub.broker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.carol.chat_pubsub.model.Topico;

public class Broker {
    private Map<String, Topico> topicos = new ConcurrentHashMap<>();

    public Topico criarTopico(String nome){
        return topicos.computeIfAbsent(nome, Topico::new);
    }

    public Topico getTopico(String nome){
        return topicos.get(nome);
    }

    public void removerTopicoSeVazio(String nome){
        Topico t = topicos.get(nome);
        if (t != null && t.vazio()) {
            topicos.remove(nome);
        }
    }
}
