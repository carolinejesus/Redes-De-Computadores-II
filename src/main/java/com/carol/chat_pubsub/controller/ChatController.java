package com.carol.chat_pubsub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carol.chat_pubsub.model.Topico;
import com.carol.chat_pubsub.service.BrokerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.carol.chat_pubsub.model.Mensagem;



@Controller
public class ChatController {
    private final BrokerService brokerService;

    public ChatController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @GetMapping("/chat")
    public String abrirChat(@RequestParam String topico, Model model) {

        Topico t = brokerService.getTopico(topico);

        if(t == null){
            return "redirect:/";
        }

        model.addAttribute("topico", topico);
        model.addAttribute("mensagens", t.getMensagens());

        return "chat";
    }

    @PostMapping("/enviarMensagem")
    public String enviarMensagem(
        @RequestParam String topico, 
        @RequestParam String mensagem) {
        Topico t = brokerService.getTopico(topico);

        if ( t != null) {
            Mensagem msg = new Mensagem(topico, "Carol", mensagem);
            t.broadcast(msg);
        } else {
            return "redirect:/";
        }
        
        return "redirect:/chat?topico=" + topico;
    }
    
    
}
