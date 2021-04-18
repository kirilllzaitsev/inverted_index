package com.parallel.computing;

import java.io.IOException;

public class ClientSession {

    public static String ipAddr = "localhost";
    public static int port = 3333;

    public static void main(String[] args) {
        Client client = new Client(ipAddr, port);

        try {
            client.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
