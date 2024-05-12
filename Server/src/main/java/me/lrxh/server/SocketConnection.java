package me.lrxh.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class SocketConnection {
    ServerSocket serverSocket;

    public SocketConnection() throws IOException {
        serverSocket = new ServerSocket(7321);

        startSocket();
    }

    public void startSocket() throws IOException {
        while (!serverSocket.isClosed()){

            //Accept the connection from the user
            Socket cleintSocket = serverSocket.accept();

            System.out.println("User connected: " + serverSocket.getInetAddress().getHostAddress());

            File file = new File("neptune.jar");
            byte[] bytes = Files.readAllBytes(file.toPath());
            Plugin packet = new Plugin("dev.lrxh.neptune.Neptune", bytes);

            packet.sendPacket(cleintSocket);
        }
    }
}
