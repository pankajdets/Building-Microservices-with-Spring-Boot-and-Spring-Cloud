package com.pankajdets.springbootkafkabasic.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    //Kafka Topic and Partition Creation
    @Bean
    public NewTopic createKafkaTopic(){
        return TopicBuilder.name("myNewTopic")
                            .build();
    }
    
}
