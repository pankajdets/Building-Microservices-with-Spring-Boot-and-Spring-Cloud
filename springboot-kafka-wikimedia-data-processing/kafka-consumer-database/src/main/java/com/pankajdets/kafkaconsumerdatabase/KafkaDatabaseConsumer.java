package com.pankajdets.kafkaconsumerdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pankajdets.kafkaconsumerdatabase.entity.WikimediaData;
import com.pankajdets.kafkaconsumerdatabase.repository.WikimediaDataRepository;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
    
    private WikimediaDataRepository wikimediaDataRepository;

    
    //construcor
    public KafkaDatabaseConsumer(WikimediaDataRepository wikimediaDataRepository) {
        this.wikimediaDataRepository = wikimediaDataRepository;
    }



    @KafkaListener(
        topics = "${spring.kafka.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumer(String eventMessage){
        LOGGER.info(String.format("event message received -> %s ",eventMessage));

        WikimediaData wikimediaData = new WikimediaData();
        wikimediaData.setWikiEventData(eventMessage); 

        wikimediaDataRepository.save(wikimediaData);

    }
    
}
