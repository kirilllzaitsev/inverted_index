package com.parallel.computing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;


public class Main {

    public static final int PORT = 3333;
    public static LinkedList<SearchServer> serverList = new LinkedList<>();
    public static Semaphore numAvailableConnections = new Semaphore(2);

    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc = new ConcurrentHashMap<>();

        IndexingController controller = new IndexingController(wordToDoc);
        controller.start();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            while (true) {
                Main.numAvailableConnections.acquire();
                Socket socket = server.accept();
                try {
                    SearchServer searchServer = new SearchServer(socket, wordToDoc);
                    serverList.add(searchServer);
                    searchServer.start();
                } finally {
                    System.out.println("Available connection slots: " +
                            Main.numAvailableConnections.availablePermits());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}