################### Apache Kafka Basic ######################################################

It ia an open-source distributed event streaming platform used by thousands of companies
for high -performance data pipelines, streaming analytics, data integration, and mission-critical 
application.

Initially built by Linkedin later managed by Apache foundation

![k1](https://user-images.githubusercontent.com/42623098/235530737-af8b3a73-5195-4a5a-92c5-78f9cd0da820.jpg)



**Basic Terms related to Kafka**

![k2](https://user-images.githubusercontent.com/42623098/235530803-7e43b87f-74a6-41f1-bd2e-58131f083e5a.jpg)

![k3](https://user-images.githubusercontent.com/42623098/235530833-3386a432-1445-48a3-b6ea-e00dd7498847.jpg)


![k4](https://user-images.githubusercontent.com/42623098/235530863-57e09d33-23bf-4939-86b6-9744ac479c6f.jpg)


![k5](https://user-images.githubusercontent.com/42623098/235530896-faa3b90f-3043-447d-93d9-f481f1fecf89.jpg)

![k6](https://user-images.githubusercontent.com/42623098/235530921-804d53e8-3d07-4acd-a089-7638fba1cb85.jpg)

![k7](https://user-images.githubusercontent.com/42623098/235530958-eeb8fbc9-7edd-4e93-8c44-a3f121259ea2.jpg)

![k8](https://user-images.githubusercontent.com/42623098/235530992-77d606b2-e2c7-4a0e-815a-46c6f14b4e72.jpg)

![k9](https://user-images.githubusercontent.com/42623098/235531019-d57df240-d14d-4c12-b8c1-2fb0693119e3.jpg)


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

![k10](https://user-images.githubusercontent.com/42623098/235531220-02d8be08-6431-4e50-96e2-3d6ef421f2ef.jpg)

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


