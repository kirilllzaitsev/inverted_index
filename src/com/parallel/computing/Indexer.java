package com.parallel.computing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class Indexer extends Thread{
    private final ConcurrentHashMap<Integer, ArrayList<String>> map;
    private final ArrayList<FileItem> files2Id;
    private final Socket tokenizerSocket = new Socket("localhost", 11030);
    private final DataOutputStream tokenizerOut = new DataOutputStream(tokenizerSocket.getOutputStream());
    private final DataInputStream tokenizerIn = new DataInputStream(tokenizerSocket.getInputStream());
    private final Logger logger = Logger.getLogger(Indexer.class.getName());

    Indexer(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc, IndexerTask task) throws IOException {
        this.map = wordToDoc;
        this.files2Id = task.getFiles2Id();
    }

    @Override
    public void run() {
        this.files2Id.stream()
                .map((fi) ->
                        CompletableFuture.supplyAsync(() -> this.processFileItem(fi))
                                .exceptionally(this::getFaultyFileItem))
                .forEach(CompletableFuture::join);
        this.logger.info("Processed " + this.files2Id.size() + " tweets");
    }

    private Boolean getFaultyFileItem(Throwable throwable) {
        this.logger.warning("Something failed when processing file\n" + throwable);
        return true;
    }

    private boolean processFileItem(FileItem fi) {
        String fileName = fi.name;

        String fileContents;
        try {
            fileContents = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            tokenizerOut.writeUTF(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        int[] tokenizedText;
        try {
            int numTokens = tokenizerIn.readUnsignedShort();
            if (numTokens == 0){
                logger.warning("Failed to tokenize text. Encoding's incompatible");
                return true;
            }
            tokenizedText = new int[numTokens];
            for (int i = 0; i < numTokens; ++i) {
                tokenizedText[i] = tokenizerIn.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        updateIndex(tokenizedText, fileName);
        return true;
    }

    private void updateIndex(int[] tokenizedText, String fileName) {
        Arrays.stream(tokenizedText).parallel().forEach(wordId ->
            map.computeIfAbsent(wordId, k -> new ArrayList<>()).add(fileName));
    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getMap() {
        return map;
    }
}
