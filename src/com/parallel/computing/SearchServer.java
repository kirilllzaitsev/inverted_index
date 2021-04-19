package com.parallel.computing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


public class SearchServer extends Thread{
    private final Socket socket;
    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;
    private final HashMap<String, Integer> wordToId = new HashMap<>();

    public SearchServer(Socket socket, ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.socket = socket;
        this.wordToDoc = wordToDoc;
    }

    protected void initWordToIdMap() {
        String vocabPath = "vocab/imdb.vocab";
        try {
            Scanner input = new Scanner(new File(vocabPath));
            int count = 0;
            while (input.hasNext()) {
                this.wordToId.put(input.next(), count);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initWordToIdMap();
        try {
            while (true) {
                DataInputStream dIn = new DataInputStream(socket.getInputStream());
                String word = dIn.readUTF();
                ArrayList<String> docs = this.wordToDoc.getOrDefault(
                        wordToId.getOrDefault(word, -1),
                        new ArrayList<>());
                this.send(docs);
            }
        } catch (NullPointerException ignored) {}
        catch (IOException e) {
            this.downService();
        }
    }

    protected void send(ArrayList<String> docsList) {
        try {
            String docsString = docsList.toString();
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(docsString);
        } catch (IOException e) {
            System.out.println("Error on server side");
            e.printStackTrace();
        }

    }

    protected void downService() {
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

    public HashMap<String, Integer> getWordToId(){
        return this.wordToId;
    }
}
