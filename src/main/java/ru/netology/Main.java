package ru.netology;

public class Main {
    public static void main(String[] args) {

        int port = 8080;
        String host = "127.0.0.1";

        Thread client = new Thread(new Client(host, port));
        Thread server = new Thread(new Server(host, port));

        client.setName("Client");
        server.setName("Server");

        server.start();
        client.start();

        try {
            server.join();
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
