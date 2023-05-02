package com.pankajdets.springbootkafkabasic.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pankajdets.springbootkafkabasic.payload.User;

@Service
public class JsonKafkaConsumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);
    
    //Spring kafka provided JsonDeserializer will convert User JSON object into Java User object
    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(User user){
        LOGGER.info(String.format("json message received -> %s", user.toString()));
    }
}
