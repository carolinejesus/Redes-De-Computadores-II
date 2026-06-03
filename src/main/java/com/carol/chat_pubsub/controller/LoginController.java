package com.carol.chat_pubsub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.carol.chat_pubsub.service.UsuarioSessaoService;

@Controller
public class LoginController {

    private UsuarioSessaoService usuarioSessao;


    public LoginController(UsuarioSessaoService usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }

    @PostMapping("/entrar")
    public String login(@RequestParam("nomeUsuario") String nomeUsuario) {
        usuarioSessao.setNomeUsuario(nomeUsuario);
        return "redirect:/topicos";
    }
}