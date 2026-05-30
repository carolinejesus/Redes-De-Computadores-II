package com.carol.chat_pubsub.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class Cliente {
    private static String topicoAtual = null;

    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Socket socket = new Socket("localhost", 12345);
             Scanner sc = new Scanner(System.in)) {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Digite seu nome: ");
            saida.println(sc.nextLine());

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        System.out.println(">>" + formatarMensagem(msg));
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao receber mensagem");
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String linha = sc.nextLine();

                if (linha.startsWith("/help")) {
                    printHelp();
                    continue;
                }

                JSONObject json = interpretarComando(linha);
                if (json != null) {
                    saida.println(json.toJSONString());
                } else {
                    System.out.println("Comando invalido.");
                }

            }
        }

    }

    private static JSONObject interpretarComando(String linha) {
        try {
            linha = linha.trim();

            if (linha.isEmpty()) {
                return null;
            }

            JSONObject json = new JSONObject();

            if (!linha.startsWith("/")) {
                if (topicoAtual == null) {
                    System.out.println("Voce ainda nao entrou em nenhum topico.");
                    System.out.println("Use: /join <topico> ou /create <topico>");
                    return null;
                }

                json.put("type", "PUBLISH");
                json.put("topic", topicoAtual);
                json.put("message", linha);
                return json;
            }

            String[] partes = linha.trim().split("\\s+", 2);
            String comando = partes[0];

            switch (comando) {

                case "/create": {
                    if (partes.length < 2) {
                        System.out.println("Uso correto: /create <topico>");
                        return null;
                    }

                    String topico = partes[1];

                    json.put("type", "CREATE_TOPIC");
                    json.put("topic", topico);

                    topicoAtual = topico;

                    return json;
                }

                case "/join": {
                    if (partes.length < 2) {
                        System.out.println("Uso correto: /join <topico>");
                        return null;
                    }
                    String topico = partes[1];

                    json.put("type", "SUBSCRIBE");
                    json.put("topic", topico);

                    topicoAtual = topico;

                    return json;
                }

                case "/use": {
                    if (partes.length < 2) {
                        System.out.println("Uso: /use <topico>");
                        return null;
                    }
                    
                    String topico = partes[1];
                    
                    json.put("type", "USE");
                    json.put("topic", topico);

                    topicoAtual = topico;

                    return json;
                }

                case "/leave": {
                    String topico;
                    if (partes.length < 2) {
                        if (topicoAtual == null) {
                            System.out.println("Voce nao esta em nenhum topico.");
                            return null;
                        }
                        topico = topicoAtual;
                    } else {
                        topico = partes[1];
                    }

                    json.put("type", "UNSUBSCRIBE");
                    json.put("topic", topico);

                    if (topico.equals(topicoAtual)) {
                        topicoAtual = null;
                    }

                    return json;
                }

                default:
                    System.out.println("Comando desconhecido.");
                    return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatarMensagem(String texto) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(texto);
            String tipo = (String) json.get("type");

            if ("MESSAGE".equals(tipo)) {
                String topico = (String) json.get("topic");
                String remetente = (String) json.get("from");
                String mensagem = (String) json.get("message");

                return "[" + topico + "] " + remetente + ": " + mensagem;
            }

            return texto;

        } catch (Exception e) {
            return texto;
        }
    }

    private static void printHelp() {
        System.out.println("Comandos disponiveis:");
        System.out.println("/create <topico>           -> criar topicos");
        System.out.println("/join <topico>             -> assinar topico");
        System.out.println("/leave <topico>            -> sair do topico");
        System.out.println("/use <topico>              -> alternar topicos");

    }
}
