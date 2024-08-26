package com.offerista.primenumbers.producer;

import com.offerista.primenumbers.producer.Producer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageApi {
    private final Producer producer;

    public MessageApi(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/message")
    public ResponseEntity<String> message(@RequestBody String message) {
        producer.send(message);
        return ResponseEntity.ok("Message received: " + message);
    }
}
