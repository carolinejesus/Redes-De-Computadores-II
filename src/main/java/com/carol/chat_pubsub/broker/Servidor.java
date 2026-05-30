package com.carol.chat_pubsub.broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(12345);
        Broker broker = new Broker();

        System.out.println("Servidor rodando...");

        while (true) {
            Socket socket = server.accept();
            new Thread(new ClientHandler(socket, broker)).start();
        }
    }
}
