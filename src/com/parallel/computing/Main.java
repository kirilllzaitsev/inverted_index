package com.parallel.computing;

import javafx.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;


public class Main {

    public static final int PORT = 3333;
    public static LinkedList<SearchServer> serverList = new LinkedList<>();
    public static Semaphore numAvailableConnections = new Semaphore(2);

    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc = new ConcurrentHashMap<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            while (true) {
                Main.numAvailableConnections.acquire();
                Socket socket = server.accept();
                try {
                    serverList.add(new SearchServer(socket, wordToDoc));
                } finally {
                    System.out.println("Available connection slots: " +
                            Main.numAvailableConnections.availablePermits());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String f11 = "datasets/aclImdb/test/neg/0_2.txt";
        String f12 = "datasets/aclImdb/test/neg/1_3.txt";
        String f21 = "datasets/aclImdb/test/neg/2_3.txt";
        String f22 = "datasets/aclImdb/test/neg/3_4.txt";
        IndexerTask indexerTask1 = new IndexerTask(new ArrayList<>(Arrays.asList(
                new Pair<>(f11, 0),
                new Pair<>(f12, 1)
        )));
        IndexerTask indexerTask2 = new IndexerTask(new ArrayList<>(Arrays.asList(
                new Pair<>(f21, 0),
                new Pair<>(f22, 1)
        )));
        Indexer i1 = new Indexer(wordToDoc, indexerTask1);
        Indexer i2 = new Indexer(wordToDoc, indexerTask2);
        i1.run();
        i2.run();

        for(int wordId: wordToDoc.keySet()){
            System.out.println(wordId);
            for (String docName:
                    wordToDoc.get(wordId)) {
                System.out.print(docName+'\t');
            }
            System.out.println();
        }

    }
}
