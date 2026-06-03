package com.carol.chat_pubsub.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.carol.chat_pubsub.broker.Broker;
import com.carol.chat_pubsub.model.Topico;

@Service
public class BrokerService {
    private final Broker broker = new Broker();

    public void criarTopico(String nomeTopico) {
        broker.criarTopico(nomeTopico);
    }

    public Broker getBroker() {
        return broker;
    }

    public Set<String> listarTopicos() {
        return broker.listarTopicos();
    }

    public Topico getTopico(String nome) {
        return broker.getTopico(nome);
    }
}
