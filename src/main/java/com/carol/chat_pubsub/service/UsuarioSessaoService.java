package com.carol.chat_pubsub.service;

import org.springframework.stereotype.Service;

@Service
public class UsuarioSessaoService {
    private String nomeUsuario;
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
