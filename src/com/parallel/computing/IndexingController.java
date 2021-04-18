package com.parallel.computing;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class IndexingController extends Thread{

    private final ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc;
    private final int MAX_TASKS_PER_INDEXER = 5;
    private final String DATA_PATH = "dataset_v2";
    private final Logger log = Logger.getLogger(IndexingController.class.getName());
    private KafkaConsumer<String, String> consumer;

    IndexingController(ConcurrentHashMap<Integer, ArrayList<String>> wordToDoc) {
        this.wordToDoc = wordToDoc;
    }

    @Override
    public void run() {
        initConsumer();
        String KAFKA_TOPIC = "index_app_topic";
        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        try {
            runConsumerLoop();
        } catch (Exception e) {
            log.info("Unexpected error" + e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void runConsumerLoop() throws IOException {
        int fileCount = 0;
        String username, text;
        ArrayList<FileItem> tasks = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
            for (ConsumerRecord<String, String> record : records) {
                JSONObject obj = new JSONObject(record.value());
                username = obj.getString("username");
                text = obj.getString("text");

                saveTweet(text, username, fileCount);
                tasks.add(composeFileItem(fileCount, username));
                fileCount++;

                if (fileCount % MAX_TASKS_PER_INDEXER == 0) {
                    new Indexer(wordToDoc, new IndexerTask(tasks)).start();
                    tasks = new ArrayList<>();
                }

            }
            consumer.commitAsync();
        }
    }

    private FileItem composeFileItem(int tweetNum, String username) {
        return new FileItem(DATA_PATH + "/" + username + "/tweet_" + tweetNum + ".txt", tweetNum);
    }

    private void saveTweet(String text, String username, int tweetNum) throws IOException {
        File file = new File("dataset_v2/" + username + "/tweet_" + tweetNum + ".txt");
        if (!file.exists()){
            file.getParentFile().mkdirs();
        }
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(text);
        printWriter.close();
    }

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