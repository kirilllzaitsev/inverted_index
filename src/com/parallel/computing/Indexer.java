package com.parallel.computing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class Indexer extends Thread{
    private final ConcurrentHashMap<Integer, ArrayList<String>> map;
    private final ArrayList<FileItem> files2Id;
    private final Socket tokenizerSocket = new Socket("localhost", 11030);
    private final DataOutputStream tokenizerOut = new DataOutputStream(tokenizerSocket.getOutputStream());
    private final DataInputStream tokenizerIn = new DataInputStream(tokenizerSocket.getInputStream());
    private final Vocab vocab = new Vocab();

    Indexer(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc, IndexerTask task) throws IOException {
        this.map = wordToDoc;
        this.files2Id = task.getFiles2Id();
    }

    @Override
    public void run() {
        for(FileItem x: files2Id){
            String fileName = x.name;

            String fileContents;
            try {
                fileContents = Files.readString(Paths.get(fileName));
                tokenizerOut.writeUTF(fileContents);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            int[] tokenizedText;
            try {
                int numTokens = tokenizerIn.readUnsignedShort();
                if (numTokens == 0){
                    System.out.println("Failed to tokenize text. Encoding's incompatible");
                    continue;
                }
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
        Arrays.stream(tokenizedText).parallel().forEach(wordId ->
            map.computeIfAbsent(wordId, k -> new ArrayList<>()).add(fileName));
    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getMap() {
        return map;
    }
}
