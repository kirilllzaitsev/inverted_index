package com.parallel.computing;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class IndexingController extends Thread{

    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;

    IndexingController(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.wordToDoc = wordToDoc;
    }

    @Override
    public void run() {
    }
}