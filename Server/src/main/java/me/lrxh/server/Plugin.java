package me.lrxh.server;

import lombok.Getter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Getter
public class Plugin {
    private final String mainClass;
    private final byte[] bytes;
    private final int length;

    public Plugin(String mainClass, byte[] bytes){
        this.mainClass = mainClass;
        this.bytes = bytes;
        this.length = bytes.length;
    }

    public  void sendPacket(Socket socket) throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
            // Write main class name
            outputStream.writeUTF(mainClass);

            // Write length of byte array
            outputStream.writeInt(length);

            // Write byte array
            outputStream.write(bytes);
        }
    }
}
