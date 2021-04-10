package com.parallel.computing;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SearchServer implements Runnable{
    private final Socket socket;
    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;

    public SearchServer(Socket socket, ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.socket = socket;
        this.wordToDoc = wordToDoc;
    }

    @Override
    public void run() {

    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getWordToDoc() {
        return wordToDoc;
    }

    public Socket getSocket() {
        return socket;
    }
}
