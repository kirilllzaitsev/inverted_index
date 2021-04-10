package com.parallel.computing;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class SearchServer  extends Thread{
    private final Socket socket;
    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;

    public SearchServer(Socket socket, ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.socket = socket;
        this.wordToDoc = wordToDoc;
    }

    @Override
    public void run() {

        try {
            while (true) {
                DataInputStream dIn = new DataInputStream(socket.getInputStream());
                String word = dIn.readUTF();
                ArrayList<String> docs = this.wordToDoc.getOrDefault(word, new ArrayList<>());
                this.send(docs);
            }
        } catch (NullPointerException ignored) {}
        catch (IOException e) {
            this.downService();
        }
    }

    private void send(ArrayList<String> docsList) {

    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                for (SearchServer vr : Main.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Main.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
        finally {
            Main.numAvailableConnections.release();
        }
    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getWordToDoc() {
        return wordToDoc;
    }

    public Socket getSocket() {
        return socket;
    }
}
