package ru.netology;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
    private String host;
    private int port;

    public Server(String host, int port) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void run() {

        ServerSocketChannel serverSocketChannel = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                System.out.println("Server socket started.");

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {

                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1) {
                        System.out.println("Server stopped.");
                        break;
                    }

                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Got message: " + msg);
                    socketChannel.write(ByteBuffer.wrap(msg.replaceAll(" ", "").getBytes(StandardCharsets.UTF_8)));
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
