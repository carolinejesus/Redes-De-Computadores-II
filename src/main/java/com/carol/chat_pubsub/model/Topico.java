package com.carol.chat_pubsub.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.carol.chat_pubsub.broker.ClientHandler;

public class Topico {
    private String nome;
    private Set<ClientHandler> inscritos = ConcurrentHashMap.newKeySet();
    
    public Topico (String nome){
        this.nome = nome;
    }
    
    public void adicionar (ClientHandler cliente){
        inscritos.add(cliente);
    }
    
    public void remover (ClientHandler cliente){
        inscritos.remove(cliente);
    }
    
    public boolean vazio(){
        return inscritos.isEmpty();
    }
    
    public void broadcast(Mensagem msg){
        for (ClientHandler c : inscritos){
            c.enviarMensagem(msg);
        }
    }
    
    public String getNome(){
        return nome;
    }
}
