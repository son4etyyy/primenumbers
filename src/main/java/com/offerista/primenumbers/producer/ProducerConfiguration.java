package com.offerista.primenumbers.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ProducerConfiguration.class);

    @Bean
    CommandLineRunner start(Producer producer) {

        return args -> {
            log.info("Running producer");
            producer.produce();

        };
    }
}
