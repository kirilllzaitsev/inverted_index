package com.parallel.computing;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


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
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        String word;
        Scanner sc = new Scanner(System.in);
        while ((word = sc.nextLine()) != null) {
            try {
                dOut.writeUTF(word);
                DataInputStream dIn = new DataInputStream(socket.getInputStream());
                String docs = dIn.readUTF();
                System.out.println(docs);
            } catch (IOException e) {
                e.printStackTrace();
                Client.this.downService();
            }
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {}
    }

}
