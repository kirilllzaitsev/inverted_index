package com.parallel.computing;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class IndexerTask {

    private final ArrayList<FileItem> files2Id;

    IndexerTask(ArrayList<FileItem> files){
        this.files2Id = files;
    }

    public ArrayList<FileItem> getFiles2Id() {
        return files2Id;
    }

    public ArrayList<String> getFileNames() {
        return (ArrayList<String>) files2Id.stream().map(x -> x.name).collect(Collectors.toList());
    }
}
