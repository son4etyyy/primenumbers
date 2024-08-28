package com.offerista.primenumbers.consumer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    private File file;

    public Consumer() {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("primeNumbers", ".csv");
            log.info("Created file in: " + tempFile.toString());
            file = new File(tempFile.toString());
        } catch (IOException e) {
            log.error("IOException " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(id = "simple-consumer", topics = "simple-message")
    public void consumeMessage(String message) {
        log.info("Got message: " + message);
        filterPrimeNumbers(Long.parseLong(message.trim()));
    }

    public void filterPrimeNumbers(Long n) {
        if(isPrime(n)) {
            boolean append = file.length() > 0;
            String numAsString = (append) ? ", " + Long.toString(n) : Long.toString(n);

            writeToFile(numAsString, append);

        }
    }

    private void writeToFile(String numAsString, boolean append) {
        try {
            FileUtils.writeStringToFile(file, numAsString, StandardCharsets.UTF_8, append);
            log.info("Appending to file: " + numAsString);
        } catch (IOException e) {
            log.error("IOException " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean isPrime(Long n) {
        if (n < 2) {
            return false;
        }
        long limit = (long) Math.sqrt(n);
        for (long i = 2; i <= limit; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
