package com.carol.chat_pubsub.broker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import com.carol.chat_pubsub.model.Mensagem;
import com.carol.chat_pubsub.model.Topico;
import com.carol.chat_pubsub.model.Usuario;
import com.carol.chat_pubsub.util.JsonUtil;

/**
 *
 * @author caroline.jesus
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private Broker broker;
    private Usuario usuario;
    private PrintWriter saida;
    private BufferedReader entrada;

    private Set<String> topicosInscritos = new HashSet<>();

    public ClientHandler(Socket socket, Broker broker) {
        this.socket = socket;
        this.broker = broker;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);

            String nome = entrada.readLine();
            usuario = new Usuario(nome);

            saida.println("Bem-vindo, " + usuario.getNome() + "!"
                    + "\nPara usar o sistema utilize algum dos comandos:"
                    + "\nPara criar topicos: /create <topico>"
                    + "\nPara alternar entre topicos: /use <topico>"
                    + "\nPara entrar em um topico: /join <topico>"
                    + "\nPar sair de um topico: /leave <topico>"
                    + "\nOu digite mensagens caso ja esteja no topico que deseja."
                    + "\nDigite '/help' a qualquer momento para obter ajuda.");

            String linha;

            while ((linha = entrada.readLine()) != null) {
                JSONObject json = JsonUtil.parse(linha);
                String tipo = (String) json.get("type");

                if (tipo == null) {
                    saida.println("Erro: comando sem tipo.");
                    continue;
                }

                switch (tipo) {
                    case "CREATE_TOPIC": {
                        String topico = (String) json.get("topic");

                        if (topico == null || topico.isBlank()) {
                            saida.println("Erro: nome do topico invalido.");
                            break;
                        }

                        broker.criarTopico(topico);
                        inscrever(topico);

                        saida.println("Topico criado: " + topico);
                        break;
                    }

                    case "SUBSCRIBE": {
                        String topico = (String) json.get("topic");

                        if (topico == null || topico.isBlank()) {
                            saida.println("Erro: nome do topico invalido.");
                            break;
                        }

                        inscrever(topico);
                        break;
                    }
                    
                    case "USE": {
                        String topico = (String) json.get("topic");

                        if (topico == null || topico.isBlank()) {
                            saida.println("Erro: nome do topico invalido.");
                            break;
                        }

                        usar(topico);
                        break;
                    }

                    case "UNSUBSCRIBE": {
                        String topico = (String) json.get("topic");

                        if (topico == null || topico.isBlank()) {
                            saida.println("Erro: nome do topico invalido.");
                            break;
                        }

                        desinscrever(topico);
                        break;
                    }

                    case "PUBLISH": {
                        String topico = (String) json.get("topic");
                        String mensagem = (String) json.get("message");

                        if (topico == null || topico.isBlank()) {
                            saida.println("Erro: topico invalido.");
                            break;
                        }

                        if (mensagem == null || mensagem.isBlank()) {
                            saida.println("Erro: mensagem vazia.");
                            break;
                        }

                        publicar(topico, mensagem);
                        break;
                    }

                    default:
                        saida.println("Erro: tipo de comando desconhecido: " + tipo);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inscrever(String nomeTopico) {
        Topico t = broker.getTopico(nomeTopico);

        if (t == null) {
            saida.println("Erro: topico nao existe: " + nomeTopico);
            return;
        }

        t.adicionar(this);
        topicosInscritos.add(nomeTopico);

        saida.println("Inscrito no topico: " + nomeTopico);
    }
    
    private void usar(String nomeTopico) { //temporario
        Topico t = broker.getTopico(nomeTopico);

        if (t == null) {
            saida.println("Erro: topico nao existe: " + nomeTopico);
            return;
        }

        t.adicionar(this);
        topicosInscritos.add(nomeTopico);

        saida.println("Agora voce esta usando o topico: " + nomeTopico);
    }

    private void desinscrever(String nomeTopico) {
        Topico t = broker.getTopico(nomeTopico);

        if (t == null) {
            saida.println("Erro: topico nao existe: " + nomeTopico);
            return;
        }

        t.remover(this);
        topicosInscritos.remove(nomeTopico);
        broker.removerTopicoSeVazio(nomeTopico);

        saida.println("Voce saiu do topico: " + nomeTopico);
    }

    private void publicar(String topico, String conteudo) {
        if (!topicosInscritos.contains(topico)) {
            saida.println("Erro: voce nao esta inscrito no topico: " + topico);
            return;
        }

        Topico t = broker.getTopico(topico);

        if (t == null) {
            saida.println("Erro: topico nao existe: " + topico);
            return;
        }

        Mensagem msg = new Mensagem(topico, usuario.getNome(), conteudo);
        t.broadcast(msg);

        //saida.println("Mensagem enviada para o topico: " + topico);
    }

    public void enviarMensagem(Mensagem msg) {
        JSONObject json = JsonUtil.mensagemToJson(msg);
        saida.println(json.toJSONString());
    }
}
