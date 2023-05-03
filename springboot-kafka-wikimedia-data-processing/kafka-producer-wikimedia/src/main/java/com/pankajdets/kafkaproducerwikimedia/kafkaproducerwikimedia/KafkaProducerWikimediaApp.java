package com.pankajdets.kafkaproducerwikimedia.kafkaproducerwikimedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaProducerWikimediaApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerWikimediaApp.class, args);
	}
	@Autowired
	private WikimediaChangesProducer wikimediaChangesProducer;

	@Override
	public void run(String... args) throws Exception {
		wikimediaChangesProducer.sendMessage();
		
	}

}
