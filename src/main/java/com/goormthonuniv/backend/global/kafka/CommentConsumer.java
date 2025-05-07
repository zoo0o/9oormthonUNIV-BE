package com.goormthonuniv.backend.global.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CommentConsumer {

    @KafkaListener(topics = "comments", groupId = "comment-group")
    public void consume(String message) {
        System.out.println("🟢 [Kafka] 새 댓글 수신: " + message);
    }
}
