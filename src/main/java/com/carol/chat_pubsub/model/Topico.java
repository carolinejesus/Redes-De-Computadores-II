package com.carol.chat_pubsub.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.carol.chat_pubsub.broker.ClientHandler;
import com.carol.chat_pubsub.model.Mensagem;

public class Topico {
    private String nome;
    private Set<ClientHandler> inscritos = ConcurrentHashMap.newKeySet();
    private List<Mensagem> mensagens = new ArrayList<>();
    
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
        mensagens.add(msg);
        for (ClientHandler c : inscritos){
            c.enviarMensagem(msg);
        }
    }
    
    public String getNome(){
        return nome;
    }
    
    public List<Mensagem> getMensagens(){
        return mensagens;
    }
}
