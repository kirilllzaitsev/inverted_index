package com.parallel.computing;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchServerTest {

    private final SearchServer searchServer = new SearchServer(null, new ConcurrentHashMap<>());

    SearchServerTest() {}

    @Test
    void initWordToIdMap() {
        searchServer.initWordToIdMap();
        assertNotNull(searchServer.getWordToId());
    }
}