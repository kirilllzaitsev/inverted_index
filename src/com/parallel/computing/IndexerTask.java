package com.parallel.computing;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class IndexerTask {

    private final ArrayList<Pair<String, Integer>> files2Id;

    IndexerTask(ArrayList<Pair<String, Integer>> files){
        this.files2Id = files;
    }

    public ArrayList<Pair<String, Integer>> getFiles2Id() {
        return files2Id;
    }

    public ArrayList<String> getFileNames() {
        return (ArrayList<String>) files2Id.stream().map(Pair::getKey).collect(Collectors.toList());
    }
}
