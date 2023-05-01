################### Apache Kafka Basic ##########################################################################

It ia an open-source distributed event streaming platform used by thousands of companies
for high -performance data pipelines, streaming analytics, data integration, and mission-critical 
application.

Initially built by Linkedin later managed by Apache foundation


**Basic Terms related to Kafka**





STEP 1: DOWNLOAD AND INSTALL KAFKA
https://dlcdn.apache.org/kafka/3.2.0/kafka_2.13-3.2.0.tgz

STEP 2: START THE KAFKA ENVIRONMENT
# Start the ZooKeeper service
C:\Users\RAMESH\Downloads\kafka>.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

# Start the Kafka broker service
C:\Users\RAMESH\Downloads\kafka>.\bin\windows\kafka-server-start.bat .\config\server.properties

STEP 3: CREATE A TOPIC TO STORE YOUR EVENTS
C:\Users\RAMESH\Downloads\kafka>.\bin\windows\kafka-topics.bat --create --topic topic_demo --bootstrap-server localhost:9092

STEP 4: WRITE SOME EVENTS INTO THE TOPIC
C:\Users\RAMESH\Downloads\kafka>.\bin\windows\kafka-console-producer.bat --topic topic_demo --bootstrap-server localhost:9092
>hello world
>topic demo

STEP 5:  READ THE EVENTS
C:\Users\RAMESH\Downloads\kafka>.\bin\windows\kafka-console-consumer.bat --topic topic_demo --from-beginning --bootstrap-server localhost:9092
hello world
topic demo


####################################################################################################################
**Use kafka in Spring Boot Application**
Step 1: Create and Setup Spring Boot Project

Step 2: Configure Kafka Producer and Consumer
    Spring Boot Provide auto configuartion to configure kafka producer and consumer

    #consumer Configurtion
    spring.kafka.consumer.bootstrap-servers=localhost:9092
    spring.kafka.consumer.group-id=myGroup
    spring.kafka.consumer.auto-offset-reset=earliest
    spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

    #producer configuarion
    spring.kafka.producer.bootstrap-servers=localhost:9092
    spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
    spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

Step 3: Create kafka topic using springboot application
        we will not create any partition. We will go with default partition

        create config package and then create KafkaTopicConfig.java inside config package
            @Configuration
            public class KafkaTopicConfig {

                @Bean
                public NewTopic createKafkaTopic(){
                    return TopicBuilder.name("myNewTopic")
                                        .build();
                }
                
            }

Step 4: Create Kafka Producer

        create package kafka and create KafkaProducer.java class
            @Service
            public class KafkaProducer {
                //Using COnstructor based dependency injection
                private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
                private KafkaTemplate<String, String> kafkaTemplate;

                public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
                    this.kafkaTemplate = kafkaTemplate;
                }

                public void sendMessage(String message){
                    LOGGER.info(String.format("Message sent %s", message));
                    kafkaTemplate.send("myNewTopic", message); 
                }
                

                
            }

Step 5: Create REST API to send message to kafka producer
        create controller package and Create MessageController.java

            @RestController
            @RequestMapping("/api/v1/kafka")
            public class MessageController {
                private KafkaProducer kafkaProducer;

                //constructor for constructor based dependency injection
                public MessageController(KafkaProducer kafkaProducer) {
                    this.kafkaProducer = kafkaProducer;
                }

                //REST API to publish message in kafka topic
                //http:localhost:8080/api/v1/kafka/publish?message=hello world
                @GetMapping("/publish")
                public ResponseEntity<String> publish( @RequestParam("message") String message){
                    kafkaProducer.sendMessage(message);
                    return ResponseEntity.ok("Message Sent to the topic");
                }
            }

Step 6: Create Kafka consumer using springboot application
        create KafkaConsumer.java in kafka package
        
            @Service
            public class KafkaConsumer {
                private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
                @KafkaListener(topics= "myNewTopic", groupId = "myGroup")
                public void consume(String message){
                    LOGGER.info(String.format("Message received -> %s", message));

                }
                
            }


