package com.parallel.computing;

import java.util.ArrayList;


public class IndexerTask {

    private final ArrayList<FileItem> files2Id;

    IndexerTask(ArrayList<FileItem> files){
        this.files2Id = files;
    }

    public ArrayList<FileItem> getFiles2Id() {
        return files2Id;
    }

}
