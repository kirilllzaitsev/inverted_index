package com.parallel.computing;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class IndexingController extends Thread{

    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;
    private KafkaConsumer<String, String> consumer;

    IndexingController(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.wordToDoc = wordToDoc;
    }

    @Override
    public void run() {}

    private void initConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "index_app");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer.encoding", "UTF-8");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);
    }
}