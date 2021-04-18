package com.parallel.computing;

import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class Indexer implements Runnable{
    private final ConcurrentHashMap<Integer, ArrayList<String>> map;
    private final ArrayList<Pair<String, Integer>> files2Id;
    private final Socket tokenizerSocket = new Socket("localhost", 11030);
    private final DataOutputStream tokenizerOut = new DataOutputStream(tokenizerSocket.getOutputStream());
    private final DataInputStream tokenizerIn = new DataInputStream(tokenizerSocket.getInputStream());

    Indexer(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc, IndexerTask task) throws IOException {
        this.map = wordToDoc;
        this.files2Id = task.getFiles2Id();
    }

    @Override
    public void run() {
        for(Pair<String, Integer> x: files2Id){
            String fileName = x.getKey();
            Integer fileId = x.getValue();

            String fileContents;
            try {
                fileContents = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
                tokenizerOut.writeUTF(fileContents);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            int[] tokenizedText;
            try {
                int numTokens = tokenizerIn.readUnsignedShort();
                tokenizedText = new int[numTokens];
                for (int i = 0; i < numTokens; ++i) {
                    tokenizedText[i] = tokenizerIn.readInt();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            updateIndex(tokenizedText, fileName);
        }
    }

    private void updateIndex(int[] tokenizedText, String fileName) {
        for(int wordId: tokenizedText){
            map.computeIfAbsent(wordId, k -> new ArrayList<>()).add(fileName);
        }
    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getMap() {
        return map;
    }
}
