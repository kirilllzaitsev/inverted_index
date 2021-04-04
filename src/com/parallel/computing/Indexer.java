package com.parallel.computing;

import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


public class Indexer implements Runnable{
    private final ConcurrentHashMap<Integer, ArrayList<String>> map;
    private final ArrayList<Pair<String, Integer>> files2Id;
    private final Socket tokenizerSocket = new Socket("localhost", 11020);
    private final DataOutputStream tokenizerOut = new DataOutputStream(tokenizerSocket.getOutputStream());
    private final DataInputStream tokenizerIn = new DataInputStream(tokenizerSocket.getInputStream());

    Indexer(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc, IndexerTask task) throws IOException {
        this.map = wordToDoc;
        this.files2Id = task.getFiles2Id();
    }

    @Override
    public void run() {
        Scanner sc;
        for(Pair<String, Integer> x: files2Id){
            String fileName = x.getKey();
            Integer fileId = x.getValue();
            try {
                sc = new Scanner(new File(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            while(sc.hasNext()) {
                try {
                    tokenizerOut.writeUTF(sc.next());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            ArrayList<Integer> tokenIds = new ArrayList<>();
            try {
                int numTokens = tokenizerIn.readInt();
                while (numTokens > 0) {
                    tokenIds.add(tokenizerIn.readInt());
                    numTokens--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Integer tokenId: tokenIds) {
                if (this.map.get(tokenId) == null) {
                    this.map.put(tokenId, new ArrayList<>(Collections.singletonList(fileName)));
                } else {
                    this.map.get(tokenId).add(fileName);
                }
            }


            System.out.println(x);
        }
    }

    public ConcurrentHashMap<Integer, ArrayList<String>> getMap() {
        return map;
    }
}
