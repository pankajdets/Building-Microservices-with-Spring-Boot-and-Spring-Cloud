package com.pankajdets.springbootkafkabasic.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.pankajdets.springbootkafkabasic.payload.User;

@Service
public class JsonKafkaProducer {
     //to fetch the json topic name from properties file
     @Value("${spring.kafka.topic-json.name}")
     private String topicJsonName;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaProducer.class);

    private KafkaTemplate<String, User> kafkaTemplate;

    //Constructor for constructor based dependency injection
    public JsonKafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(User data){
        LOGGER.info(String.format("Message sent -> %s", data.toString()));
        Message<User> message = MessageBuilder.withPayload(data)
                                .setHeader(KafkaHeaders.TOPIC, topicJsonName)
                                .build();
        
        kafkaTemplate.send(message);
    }
    
    

    
}
