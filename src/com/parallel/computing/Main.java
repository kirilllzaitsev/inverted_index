package com.parallel.computing;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class Main {

    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc = new ConcurrentHashMap<>();
        String f1 = "datasets/aclImdb/test/neg/0_2.txt";
        String f2 = "datasets/aclImdb/test/neg/1_3.txt";
        IndexerTask indexerTask = new IndexerTask(new ArrayList<>(Arrays.asList(
                new Pair<>(f1, 0),
                new Pair<>(f2, 1)
        )));
        Indexer i1 = new Indexer(wordToDoc, indexerTask);
        i1.run();
    }
}
