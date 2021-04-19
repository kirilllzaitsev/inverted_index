package com.parallel.computing;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexerTest {

    private final Indexer indexer = new Indexer(new ConcurrentHashMap<>(), new IndexerTask(new ArrayList<>()));

    IndexerTest() throws IOException {}

    @Test
    void getFaultyFileItem() {
        assertEquals(indexer.getFaultyFileItem(new Exception()), true);
    }
}