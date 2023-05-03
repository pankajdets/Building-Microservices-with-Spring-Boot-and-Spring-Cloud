################## Event Driven Microservices sung SpringBoot and Kafka #######################
-Asynchronous microservice communication using Kafka

Step 1: Create 4 microservices order-service, stock-service, email-service and base-domains

-In  order-service, stock-service & email-service
    -Add dependency Spring Web and Spring for Apache kafka
-In base-domains
    -Add only lombok dependency


order-service Port: 8080
stock-service Port : 8081
email-service Port : 8082


Step 2: Base-Domains Microservice- Create DTO classes-Order and OrderEvent


        -create dto package and create Order.java
        @Data //create getters , setters, toString(), Hahcode() and equal()
        @AllArgsConstructor
        @NoArgsConstructor
        public class Order {
            private String orderId;
            private String name;
            private int qty;
            private double price;
            
        }

    -Create OrderEvent.java inside dto package
    
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class OrderEvent {
            private String message;
            private String status;
            private Order order;
            
        }

Step 2: order-service microservice- Configure Kafka Producer
        # Telling that kafka broker is running on 9092
        spring.kafka.producer.bootstrap-servers=localhost:9092
        spring.kafka.producer.key-serializer= org.apache.kafka.common.serialization.StringSerializer
        spring.kafka.producer.value-serializer= org.springframework.kafka.support.serializer.JsonSerializer
        spring.kafka.topic.name=order_topic

Step 3: order-service microservice- Configure Kafka Topic
    -create config package and create KafkaTopicConfig.java
        @Configuration
        public class KafkaTopicConfig {

            @Value("${spring.kafka.topic.name}")
            private String topicName;

            //spring bean for kafka topic
            @Bean
            public NewTopic topic(){
                return TopicBuilder.name(topicName).build();
            }
            
        }


Step 4: order-service microservice- Create Kafka Producer
        -Create package kafka and create OrderProducer.java
            @Service
            public class OrderProducer {

                private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

                private NewTopic topic;
                private KafkaTemplate<String, OrderEvent>  kafkaTemplate;

                public OrderProducer(NewTopic topic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
                    this.topic = topic;
                    this.kafkaTemplate = kafkaTemplate;
                }

                public void sendMessage(OrderEvent event){
                    LOGGER.info(String.format("Order event => %s", event.toString()));

                    //Create message
                    Message<OrderEvent> message = MessageBuilder.withPayload(event)
                                                            .setHeader(KafkaHeaders.TOPIC, topic.name())
                                                            .build();

                    kafkaTemplate.send(message);

                }  
            }



Step 4: order-service microservice- Create REST API to send order and test Kafka producer
        -Create controller package and Create OrderController.java
                @RestController
                @RequestMapping("/api/v1")
                public class OderController {
                    private OrderProducer orderProducer;

                    public OderController(OrderProducer orderProducer) {
                        this.orderProducer = orderProducer;
                    }

                    @PostMapping("/orders")
                    public String placeOrder(@RequestBody Order order){
                        order.setOrderId(UUID.randomUUID().toString());
                        OrderEvent orderEvent = new OrderEvent();
                        orderEvent.setStatus("PENDING");
                        orderEvent.setMessage("order status is in pending state");
                        orderEvent.setOrder(order);

                        orderProducer.sendMessage(orderEvent);

                        return "Order Placed Successfully";

                    }

                    
                }

After hiting REST API(POST) it is sending OrderEvent in kafaka broker

Step 5: stock-service microservice- Configure kafka Consumer
                spring.kafka.consumer.bootstrap-servers=localhost:9092
                #Whenever multiple consumers consuming message from Single kafka topics make sure to assign different group to these consumers
                spring.kafka.consumer.group-id=stock
                spring.kafka.consumer.auto-offset-reset=earliest
                spring.kafka.consumer.key-deserializer= org.apache.kafka.common.serialization.StringDeserializer
                spring.kafka.consumer.value-deserializer= org.springframework.kafka.support.serializer.JsonDeserializer

                #Spring kafka library will trust these packages for json serialization and deserialization
                spring.kafka.consumer.properties.spring.json.trusted.packages=*

                spring.kafka.topic.name=order_topic


Step 6: stock-service microservice- Create Kafka Consumer
        -Create kafka package and Create OrderConsumer.java
            @Service
            public class OrderConsumer {
                private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

                @KafkaListener(topics="${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
                public void consume(OrderEvent event){
                    LOGGER.info(String.format("Order event recieved in stock service => %s", event.toString()));

                    //save the order event data into the database

                }
                
            }


Step 7: email-service microservice- Configure and Create Kafka Consumer
        spring.kafka.consumer.bootstrap-servers=localhost:9092
        #Whenever multiple consumers consuming message from Single kafka topics make sure to assign different group to these consumers
        spring.kafka.consumer.group-id=email
        spring.kafka.consumer.auto-offset-reset=earliest
        spring.kafka.consumer.key-deserializer= org.apache.kafka.common.serialization.StringDeserializer
        spring.kafka.consumer.value-deserializer= org.springframework.kafka.support.serializer.JsonDeserializer

        #Spring kafka library will trust these packages for json serialization and deserialization
        spring.kafka.consumer.properties.spring.json.trusted.packages=*

        spring.kafka.topic.name=order_topic



        -Create kafka package and Create OrderConsumer.java
            @Service
            public class OrderConsumer {
                private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

                @KafkaListener(topics="${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
                public void consume(OrderEvent event){
                    LOGGER.info(String.format("Order event recieved in email service => %s", event.toString()));

                     //send email to customer

                }
                
            }


Step 8: Run all 3 microservices together and test




############################################################################################################