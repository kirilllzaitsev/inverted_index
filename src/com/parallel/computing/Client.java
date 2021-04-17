package com.parallel.computing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {

    private Socket socket;
    BufferedReader in;

    public Client(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
    }

    public void send() throws IOException {
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {}
    }

}
