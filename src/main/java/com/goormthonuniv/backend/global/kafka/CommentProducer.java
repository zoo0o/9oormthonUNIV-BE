package com.goormthonuniv.backend.global.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "comments"; // Kafka 토픽 이름

    public void sendComment(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
