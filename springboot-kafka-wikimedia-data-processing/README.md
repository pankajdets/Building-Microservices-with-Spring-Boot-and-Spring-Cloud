**Wikimedia stream to database**
#Main purpose of this project to demonstrate the usage of apache kafka

We will read huge amout of real time data from wikimedia to the database
Kafka Producer: Will read real time stream data from wikimedia-, then  itw ill write the data to kafka broker 
Kafka Consumer: Will consume real time stream data from kafka broker, then it will write to the database


https://stream.wikimedia.org/v2/stream/recentchange
This REST API retrieve the changes done by users


Step 1: 
Create Multi module maven project (springboot-kafka-wikimedia-data-processing) with two sub projects (kafka-producer-wikimedia) and (kafka-consumer-database)

-To create Multi module maven project (springboot-kafka-wikimedia-data-processing) 
        create springboot project from spring initializer as below


        Goto pom.xml file and add packaging 
        <packaging>pom</packaging>
        <modules>
		    <module>kafka-producer-wikimedia</module>
            <module>kafka-consumer-database</module>
	    </modules>

- Update Parent information in child projects
        <parent>
            <groupId>com.pankajdets</groupId>
            <artifactId>springboot-kafka-wikimedia-data-processing</artifactId>
            <version>0.0.1-SNAPSHOT</version>
	    </parent>
        

- Create sub project i.e springboot project (kafka-producer-wikimedia) inside (springboot-kafka-wikimedia-data-processing)
        Goto pom.xml file and add packaging 
        <packaging>jar</packaging>
        parent>
            <groupId>com.pankajdets</groupId>
            <artifactId>springboot-kafka-wikimedia-data-processing</artifactId>
            <version>0.0.1-SNAPSHOT</version>
	    </parent>


Step 2: Configure kafka-producer-wikimedia and create a Topic

    -Create  KafkaTopicConfig.java and create a bean
        @Configuration
        public class KafkaTopicConfig {

            @Bean
            public NewTopic topic(){
                return TopicBuilder.name("wikimedia_recentchange").build();
            }
            
        }

Step 3: wikimedia kafka producer implementation
    -Create WikimediaChangesProducer.java file
    -To Read real time stream data from wikimedia, we use event source
        for that we need okhttp-eventsource library
            <!-- https://mvnrepository.com/artifact/com.launchdarkly/okhttp-eventsource -->
            <dependency>
                <groupId>com.launchdarkly</groupId>
                <artifactId>okhttp-eventsource</artifactId>
                <version>4.1.0</version>
            </dependency>
            <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
            <dependency>
                <groupId>com.squareup.okhttp3</groupId>
                <artifactId>okhttp</artifactId>
                <version>4.11.0</version>
            </dependency>

    - We also need jackson library to deal with Json
            <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-core</artifactId>
                <version>2.15.0</version>
            </dependency>

            <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>2.15.0</version>
            </dependency>


            @Service
            public class WikimediaChangesProducer {

                private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);
            
                private KafkaTemplate<String, String> kafkaTemplate;

                public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
                    this.kafkaTemplate = kafkaTemplate;
                }

                public void sendMessage() throws InterruptedException{
                    String topic = "wikimedia_recentchange";
                    
                    //To Read real time stream data from wikimedia, we use event source
                    EventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, topic);
                    String url ="https://stream.wikimedia.org/v2/stream/recentchange";
                    EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
                    EventSource eventSource = builder.build();
                    eventSource.start();

                    TimeUnit.MINUTES.sleep(10);
                }   
            }


        - create class WikimediaChangesHandler which implements EventHandler interface and 
        - implemented onMessage() method- on message log and send message to kafka topic

                public class WikimediaChangesHandler implements EventHandler {
                private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesHandler.class);
                private KafkaTemplate<String, String> kafkaTemplate;
                private String topic;

                

                public WikimediaChangesHandler(KafkaTemplate<String, String> kafkaTemplate, String topic) {
                    this.kafkaTemplate = kafkaTemplate;
                    this.topic = topic;
                }

                @Override
                public void onClosed() throws Exception {
                
                }

                @Override
                public void onComment(String arg0) throws Exception {
                
                }

                @Override
                public void onError(Throwable arg0) {
                
                }

                @Override
                public void onMessage(String s, MessageEvent messageEvent) throws Exception {
                    LOGGER.info(String.format("event data -> %s", messageEvent.getData()));

                    //Send event to kafka topic

                    kafkaTemplate.send(topic, messageEvent.getData());

                    
                }

                @Override
                public void onOpen() throws Exception {
                
                }
                
            }




#####################################################################################################################
**Kafka Consumer Project Setup**
Step 1:     Create Spring boot application "kafka-consumer-database" inside parent project and updated parent info in pom.xml
    and update module in parent project "springboot-kafka-wikimedia-data-processing"

    <packaging>jar</packaging>


Step 2: Configure Kafka Consumer in application.properties file
        spring.kafka.consumer.bootstrap-servers=localhost:9092
        spring.kafka.consumer.group-id=myGroup
        spring.kafka.consumer.auto-offset-reset=earliest
        spring.kafka.consumer.key-deserializer= org.apache.kafka.common.serialization.StringDeserializer
        spring.kafka.consumer.value-deserializer= org.apache.kafka.common.serialization.StringDeserializer

Step 3: Kafka Consumer Implementation 

        @Service
        public class KafkaDatabaseConsumer {
            private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);

            @KafkaListener(
                topics = "wikimedia_recentchange",
                groupId = "myGroup"
            )
            public void consumer(String eventMessage){
                LOGGER.info(String.format("event message received -> %s ",eventMessage));

            }
            
        }

Step 4: Configure MySQL Database
        create database "wikimedia" in MySQL

        Add depenedency in pom.xml
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-data-jpa</artifactId>
                </dependency>

                <dependency>
                    <groupId>com.mysql</groupId>
                    <artifactId>mysql-connector-j</artifactId>
                    <scope>runtime</scope>
                </dependency>

        add below properties in application.properties file

                ###########################MYSQL#################
                spring.datasource.url=jdbc:mysql://localhost:3306/wikimedia
                spring.datasource.username=root
                spring.datasource.password=Munnu@10Oct

                spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQLDialect
                spring.jpa.hibernate.ddl-auto=update

                #####To see hibernate generated sql statement in console########
                spring.jpa.properties.hibernate.show_sql=true
                spring.jpa.properties.hibernate.use_sql_comments=true
                spring.jpa.properties.hibernate.format_sql=true

Step 5: Save wikimedia data in MySQL Database
        -Create JPA Entity to save into database
            @Getter
            @Setter
            @Entity
            @Table(name= "wikimedia_recentchange")
            public class WikimediaData {
                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Long id;

                //@Lob //specifies that the database should store the property as Large Object.
                @Column(columnDefinition="LONGTEXT") 
                private String wikiEventData;
            }


        -Create WikimediaDataRepository which extends from JpaRepository

            public interface WikimediaDataRepository extends JpaRepository<WikimediaData, Long> {
        
            }
    
Step 6: Implement KafkaDatabaseConsumer class

        @Service
        public class KafkaDatabaseConsumer {
            private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
            
            private WikimediaDataRepository wikimediaDataRepository;

            
            //construcor
            public KafkaDatabaseConsumer(WikimediaDataRepository wikimediaDataRepository) {
                this.wikimediaDataRepository = wikimediaDataRepository;
            }



            @KafkaListener(
                topics = "wikimedia_recentchange",
                groupId = "myGroup"
            )
            public void consumer(String eventMessage){
                LOGGER.info(String.format("event message received -> %s ",eventMessage));

                WikimediaData wikimediaData = new WikimediaData();
                wikimediaData.setWikiEventData(eventMessage); 

                wikimediaDataRepository.save(wikimediaData);

            }
            
        }



Step 7: Refactor code to Externalize the Topic name - Remove hard coded values

#################################################################################################################

