package com.pankajdets.springbootkafkabasic.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    //to fetch the topic name from properties file
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    //to fetch the json topic name from properties file
    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;

    //Kafka Topic and Partition Creation
    @Bean
    public NewTopic createKafkaTopic(){
        return TopicBuilder.name(topicName)
                            .build();
    }

    @Bean
    public NewTopic createKafkaJsonTopic(){
        return TopicBuilder.name(topicJsonName)
                            .build();
    }
    
}
