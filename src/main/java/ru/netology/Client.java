package ru.netology;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable {

    int port;
    String host;

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void run() {

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        SocketChannel socketChannel = null;

        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Client socket started.");

            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("CLIENT: Enter message for server: ");
                msg = scanner.nextLine();
                if ("end".equals(msg)) {
                    System.out.println("Client asked to stop.");
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(1000);
                System.out.println("Message: " + msg);

                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println("SERVER: " + new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                inputBuffer.clear();

            }

        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

