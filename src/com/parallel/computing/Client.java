package com.parallel.computing;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;


public class Client {

    private Socket socket;
    BufferedReader in;

    public Client(String addr, int port) {
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
