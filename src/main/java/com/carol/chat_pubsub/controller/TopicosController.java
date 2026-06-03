package com.carol.chat_pubsub.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.carol.chat_pubsub.service.BrokerService;
import com.carol.chat_pubsub.service.UsuarioSessaoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicosController {
    private UsuarioSessaoService usuarioSessao;
    private BrokerService brokerService;

    public TopicosController(UsuarioSessaoService usuarioSessao, BrokerService brokerService) {
        this.usuarioSessao = usuarioSessao;
        this.brokerService = brokerService;
    }
    
    @GetMapping("/topicos")
    public String topicos(Model model) {

        model.addAttribute(
                "nome",
                usuarioSessao.getNomeUsuario()
        );
        model.addAttribute(
            "topicos", brokerService.listarTopicos()
        );

        return "topicos";
    }

    @PostMapping("/criarTopico")
    public String criarTopico(@RequestParam("nomeTopico") String nomeTopico) {
        brokerService.criarTopico(nomeTopico);
        return "redirect:/topicos";
    }

}
