package com.offerista.primenumbers.producer;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Producer {
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    public static final int STREAM_SIZE = 100;

    private KafkaTemplate<String, String> producer;

    public Producer(KafkaTemplate<String, String> simpleProducer) {
        this.producer = simpleProducer;
    }
    public void send(String message) {
        producer.send("simple-message", message);
    }
    public void produce() {
        Random random = new Random();
        IntStream intStream = random.ints(STREAM_SIZE);
        String stringData = intStream
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        String[] parts = stringData.split(Pattern.quote(","));
        // send to listener
        long waitTime = 5000;
        for(String s: parts) {
            send(s);
            try {
                Thread.sleep(waitTime); //up to 5 numbers per second / filled stream size of maximum 100 numbers
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        File targetFile;
        try {
            targetFile = Files.createTempFile("primeNumbers", ".csv").toFile();
            FileUtils.writeStringToFile(targetFile, stringData, StandardCharsets.UTF_8);
            log.info("Writing to file: " + stringData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
