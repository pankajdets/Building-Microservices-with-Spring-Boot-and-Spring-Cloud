Microservice Applications and It's Port Mapping
We will be creating a lot of microservices so please refer below ports mapping (microservice applications with their ports):

For the API-Gateway application, use the 9191 port.

For the Department-Service application, use the 8080 port and for its instance, use port 8082

For the Employee-Service application, use the 8081 port.

For the Config-Server application, use the 8888 port.

For the Service-Registry application, use the 8761 port.

For the Organization-Service application, use the 8083 port.

For the React-Frontend application, use the 3000 port.

Zipkin Server uses the default port 9411

![M4](https://user-images.githubusercontent.com/42623098/233919456-b00335f4-f962-4688-a414-2d60cc0a1e2d.jpeg)

![M3](https://user-images.githubusercontent.com/42623098/233919675-d122042a-ef48-4a8c-9b20-22ad9825c1e0.jpg)


**Start Building**

Create Two Microservice Employee Service and Department service
![image](https://user-images.githubusercontent.com/42623098/233922989-dbabe9dc-c727-4a99-baa6-f329b6e50d2c.png)

![image](https://user-images.githubusercontent.com/42623098/233923138-cea3e3fb-fc04-4608-905b-d1926b9f20c8.png)

##############################################################################################

**Configure And Develop REST APIs in department-service**


Setup Database connection in department-service
    Create database department_db
    Add below properties in application.properties file
        spring.datasource.url=jdbc:mysql://localhost:3306/department_db
        spring.datasource.username= root
        spring.datasource.password=Munnu@10Oct

        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
        spring.jpa.hibernate.ddl-auto=update

Create Department JPA Entity and Spring Data JPA Repository in department-service
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Entity
        @Table(name ="departments")
        public class Department {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            private String departmentName;
            private String departmentDescription;
            private String departmentCode;
        }

Build Save Department REST API in department-service
Build Get Department REST API in department-service

#########################################################################################

**Configure And Develop REST APIs in employee-service**

Setup Database connection in employee-service
    Create database employee_db
    Add properties in application.properties file

Create Employee JPA Entity and Spring Data JPA Repository
Build Save Employee REST API in employee-service
Build Get Employee REST API in employee-service

##################################################################################################
**Assignments**

**Assignment 1**

1. Use ModelMapper and MapStruct Mapping Libraries in EmployeeService to convert the Employee JPA entity to EmployeeDto and Vice Versa.

2. Use ModelMapper and MapStruct Mapping Libraries in DepartmentService to convert the Department JPA entity to DepartmentDto and Vice Versa.

**Assignment 2**
Exception Handling in department-service and employee-service

1. Create ErrorDetails class to hold the custom error response
2. Create ResourceNotFoundException in EmployeeService and use this ResourceNotFoundException in the getEmployeeById() method in EmployeeServiceImpl class
3. Create GlobalExceptionHandler class in EmployeeService to handle specific ResourceNotFoundException and global exceptions in a single place.
4. Create ResourceNotFoundException in DepartmentService and use this ResourceNotFoundException in the getDepartmentByCode() method in DepartmentServiceImpl class
5. Create GlobalExceptionHandler in DepartmentService to handle specific ResourceNotFoundException and global exceptions in a single place.

**What are important annotations for handling Exceptions in the Spring Boot application?**

Spring provides two important @ExceptionHandler and @ControllerAdvice annotations to handle the exceptions (controller level as well as globally) and returns the custom error response back to the client.
The @ExceptionHandler is an annotation used to handle the specific exceptions and send the custom responses to the client.
The @ControllerAdvice is an annotation, to handle the exceptions globally.





##################################################################################################

Microservices Communication

3 Different Ways
1. Rest Template
2. WebClient
3. Spring Cloud OpenFeign

Two Kinds of communication Styles
1. Synchronous Communication
2. Asynchronous Communication

1. Synchronous COmmunication: 

    1. The Client sends a request and waits for a response from the service.
    2. The important point here is that the protocol (HTTP/HTTPS) is synchronous and the client code can only continue its task when it receives the HTTP server response
    3. We can acheive synchronous communication using RestTemplate, WebClient and Spring Cloud Open Feign library

2. Asynchronous Asynchronous
    1. The client sends a request and does not wait for a response from the service
    2. The client will continue executing its task-It don't wait for the response from service
    3. In case ofv Asynchronous Asynchronous, the client and server uses middle component called "Message Broker" example RabbitMQ or Apache Kafka

############################################################################################

**1. microservice Communication using RestTemplate**

Make a REST API call from employee-service to department-service using RestTemplate

**Requirements**


![R1](https://user-images.githubusercontent.com/42623098/234000234-90e71313-c811-4a84-8861-606de806643a.jpg)



**Development Steps**

![R2](https://user-images.githubusercontent.com/42623098/234000266-65945ffd-f145-4d36-a6dc-51ab66e2cac4.jpg)



Step 1: Add departmentCode field in Employee JPA Entity
    public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String departmentCode;   
    }

Step 2: Create DepartmentDto class in employee-service
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class DepartmentDto {
        private Long id;
        private String departmentName;
        private String departmentDescription;
        private String departmantCode;

        
    }

Step 3: Configure RestTemplate as spring bean
    Add below code in Entry point class

    @Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

Step 4: Inject and Use RestTemplate to make REST API call in EmployeeServiceImpl class

    private RestTemplate restTemplate; //Constructor based dependency injection

    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {
        
       Employee employee =  employeeRepository.findById(employeeId).get();

       ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/"+employee.getDepartmentCode(), DepartmentDto.class);

        DepartmentDto departmentDto = responseEntity.getBody();
       //Convert Employee JPA Entity to EmployeeDto

       EmployeeDto employeeDto = new EmployeeDto(
        employee.getId(),
        employee.getFirstName(),
        employee.getLastName(),
        employee.getEmail(),
        employee.getDepartmentCode()
       );

       APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto);
       return apiResponseDto;
    }


############################################################################################################################
**Important Methods in Resttemplate**










###############################################################################################


Note:  As of 5.0, the RestTemplate class is in maintenance model and soon will be deprecated. So the Spring team recommended using  WebClient that has a modern API and support sync, async, and streaming scenarios.


###############################################################################################

**Microservice Comminication Using WebClient**

Make a REST API call from employee-service to department-service

Development Steps:
1. Add Spring WebFlux Dependency
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>
2. Configure WebClient as Spring Bean
        @Bean
        public  WebClient webClient(){
            return WebClient.builder().build();
        }
3. inject and use WebClient to Call the Rest API
        private WebClient webClient; //Constructor injection

         DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/"+employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

4. Test using postman client


######################################################################################################
**Important Methods in WebClient**




######################################################################################################


**Microservice Comminication Using Spring Cloud Open Feign**


Make a REST API call from employee-service to department-service

**Development Steps**
1. Add Spring Cloud open feign Maven dependency to employee-service
        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>

    we  we have to add spring cloud starter related dependency, We need to also add dependencyManagement in pom.xml file

    <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  Need to add spring-cloud.version version as well

  <properties>
		<spring-cloud.version>2022.0.2</spring-cloud.version>
  </properties>


2. Enable Feign Client using @EnableFeignClients
    Annotate main entry class with @EnableFeignClients
    // Enables components scanning for interfaces that declare they are Feign clients

3. Create Feign API Client
    Create APIClient interface in service package

        @FeignClient(url = "http://localhost:8080", value = "DEPARTMENT-SERVICE")
        public interface APIClient {
            //Open Feign library will dynamically create implementation for this interface
            @GetMapping("api/departments/{department-code}")
            DepartmentDto getDepartment(@PathVariable("department-code") String departmentCode); 
        }



4. Change the getEmployeeById  method in EmployeeServiceImpl class  to use APIClient
        private APIClient apiClient;

        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

5. Test using postman

######################################################################################################

**Important Methods in Open Feign library**




######################################################################################################

**Understanding Service Registry**

![s1](https://user-images.githubusercontent.com/42623098/234043325-557a6a52-d921-4953-96f8-8293ddcc8118.jpg)



![s2](https://user-images.githubusercontent.com/42623098/234043384-3f10bd2a-bf60-4da7-b8b6-1cc2204929af.jpg)


Developmentv Steps

![s3](https://user-images.githubusercontent.com/42623098/234043417-0aaea338-e7e5-427a-8c33-6d110385dab9.jpg)


1. Create Spring boot project as Microservice(service-registry)

![image](https://user-images.githubusercontent.com/42623098/234043574-91058dc1-7ebd-401f-8142-516a53d7edad.png)


2. Add @EnableEurekaServer annotation
    Annotate service-registry entry class with @EnableEurekaServer 
    // This will make this spring boot project as Eureka Server

3. Disable Eureka Server as Eureka Client
    Eureka Server is also a Eureka client and we need to disable this behaviour
    Add below properies in application.properties file

        spring.application.name=SERVICE-REGISTRY
        server.port=8761
        #By default, each Eureka Server is also a Eureka Client and We need to disable client-side behaviour
        #by configuring these properties in the application.properties file
        eureka.client.register-with-eureka=false
        eureka.client.fetch-registry=false

        We can see Srping Eureka UI at http://localhost:8761

4. Launch Eureka Server  and check at http://localhost:8761

5. Registering department-service microservice as Eureka Client
        Add below dependency in  depart-service
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>


            <properties>
                 <spring-cloud.version>2022.0.2</spring-cloud.version>
            </properties>

            <dependencyManagement>
                <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>${spring-cloud.version}</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                </dependencies>
            </dependencyManagement>

   
        set department-service name in application.properties file
            spring.application.name= DEPARTMENT-SERVICE
6. Run department-service Eureka Client- running successfully
7. Registering employee-service microservice as Eureka Client
        Add dependency same as department service andrest it will auto config
        set application name
            spring.application.name= EMPLOYEE-SERVICE

8. Running Multiple instance of department-service

    Generate jar file for department-service
        goto department-service directory using cd command
        and run command (mvn install)  to generate jar file for department service
    
    Run jar file
        java -jar -Dserver.port=8082 target/jarfile.jar
        It will run new instance of department-service on port 8082
    
    We can see it on Eureka Service UI

    Try accessing both the instance of department-service running on port 8080 and 8082


 Another way to run multiple instancse
 run first instance and then change port number in application.properties file and run run 2nd instance   

########################################################################################################
**Load Balancing with Eureka Open Feign**

Now we have two instance of department-service up and running we can call both the instance from
employee-service. Let's see how to load balance between these two instance of department-service

![l1](https://user-images.githubusercontent.com/42623098/234206769-2ddf964e-f69e-4d5f-ab3a-598e02e8765b.jpg)


In order to load balance we just have to change the url of Open Feign Client annotation in APIClient interface

@FeignClient(url = "http://localhost:8080", value = "DEPARTMENT-SERVICE")

@FeignClient(name = "DEPARTMENT-SERVICE")

DEPARTMENT-SERVICE is service id of department-service. Eureka server will automatically take 
application name as service id.

now test and see if employee-service in calling both the instance of deprat-service alternatevly
#####################################################################################################

**Understanding API Gateway**

![a1](https://user-images.githubusercontent.com/42623098/234592486-e083a200-a671-4f86-9f9a-3f51677807c2.jpg)

![a2](https://user-images.githubusercontent.com/42623098/234592535-e960a94a-d372-486d-a901-525aae24d5d4.jpg)

![a3](https://user-images.githubusercontent.com/42623098/234592598-61440f7c-186d-4472-9186-37959013c02b.jpg)

![a4](https://user-images.githubusercontent.com/42623098/234592812-a712e581-4f17-4b06-a036-5a40524bc90c.jpg)

Development Steps

![a5](https://user-images.githubusercontent.com/42623098/234592918-3937f5da-b995-456c-85c1-4a7bb687f229.jpg)

1. Create and Setup API Gateway Microservice

![image](https://user-images.githubusercontent.com/42623098/234593880-6a8ba5b9-49b5-4889-aef9-1962c0ee142b.png)


2. Register API-Gateway as Eureka Client to Eureka Server
    Add below code to application. properties file
        spring.application.name=API-GATEWAY
        server.port=9191
    Rest it will auto configure

3. Configure API Gateway Routes and Test using Postman client

When client sends request to API Gateway then API Gateway will discover the correct service ip address and port using service registry to communicate and route the request that is why we have register API gateway as Eureka Client

We can configure routes in two ways
1. Using Properties
2. Using program


1. Using Properties
    Adding below routing information in application.properties file of API-GATEWAY service
    #Rouest for Employee Service
    spring.cloud.gateway.routes[0].id=EMPLOYEE-SERVICE
    spring.cloud.gateway.routes[0].uri=lb://EMPLOYEE-SERVICE
    spring.cloud.gateway.routes[0].predicates[0]=Path=/api/employees/**

    #Rouest for Department Service
    spring.cloud.gateway.routes[1].id=DEPARTMENT-SERVICE
    spring.cloud.gateway.routes[1].uri=lb://DEPARTMENT-SERVICE
    spring.cloud.gateway.routes[1].predicates[0]=Path=/api/departments/**

Configure below properties in all eureka clients to resolve machine DNS problem
    eureka.instance.prefer-ip-address=true

4. Using Spring Cloud Gateway to Automatically Creates Routes

    Comment manual route information which is done in step 3

    Add below properties

        # Flag that enables DiscoveryClient gateway integration. Auto Configure route
        #hence we have commented manually configured route info
        spring.cloud.gateway.discovery.locator.enabled=true

        #Option to lower case serviceId in  predicates and filters, defaults to false. 
        #Useful with eureka when it automatically uppercases serviceId. 
        #so MYSERIVCE, would match /myservice/**
        spring.cloud.gateway.discovery.locator.lower-case-service-id=true

        # This will help us to see debug log of route mapping
        logging.level.org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping=DEBUG



        Add service name in url

        http://localhost:9191/employee-service/api/employees/2
        http://localhost:9191/department-service/api/departments/IT



We will follow creating routes manually


#####################################################################################################

**Centralized Configurations using Spring Spring Cloud Config Server**

Problem: Consider we have multiple microservices and in order to scale microservice we started multiple instances of each microservices. Each microservice has configuration file which contains microservice level configuration. 

Now if we have requirement to update configuration file in microservice 1. Then we need to start microservice 1 and ints instances which is not a good practice. This is not agood idead that each and every time we update configuraion we start microservice and its instances.

**Spring Cloud Config Server** addresses this problem. If we use Spring Cloud Config Server we don't need to restart microservices and its instances.

Spring Cloud Config Server also help to externalize the configuration file of each and every microservices. We can externaliza all the config file of all the microservices in a central reposirty(say Git). We don't need to go into each microservice to update configuration. We can simple change in central git repository


**Spring Cloud Config Service is nothing but spring Boot project with spring cloud config server dependency**


![cs1](https://user-images.githubusercontent.com/42623098/234888886-ff8dca16-1b5a-437a-9066-7fdeaa1b2660.jpg)


Development Steps:

![cs2](https://user-images.githubusercontent.com/42623098/234888946-3d4882cc-00de-4664-89e6-023c014402ed.jpg)

Step 1: Create Spring boot project  as Microservice(config server)

![image](https://user-images.githubusercontent.com/42623098/234889035-a8fee096-be26-42c7-a1c9-5b034a5cf70f.png)





Annotate main entry point class of config-server with @EnableConfigServer

Add below properties in application.properties file
        spring.application.name=CONFIG-SERVER
        server.port=8888


step 2: Register, Config-Server as Eureka Client
        Eureka Client dependecy already added. Rest it will auto configure

Step 3: Setup Git location for Config Server
        Create git repository with name "config-server-repo"
        and below property in application.properties file to configure git location
            spring.cloud.config.server.git.uri=https://github.com/pankajdets/config-server-repo
        
        Need to clone this git repository when application start up so that add below property
            spring.cloud.config.server.git.clone-on-start=true

        In git repository mainly two branch  master and main. We will keep all the configuration file in main branch
            spring.cloud.config.server.git.default-label=main
Step 4: Refactor Department-Service to use Config-Server

        Add Config client and actuator dependecy in pom.xml of Department-Service
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-actuator</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-config</artifactId>
                </dependency>
        
        Now Create Project(Service) specific configuration file in git repository.
        Give the file name should be same as service name like department-service.properties
        and paste configure there.We can give file name in lower case as well.


        Now comment all the properties in application.properties file except
            spring.application.name= DEPARTMENT-SERVICE
        Because department-service uses this application name to load the configuarion file from config-server


        Now need to configure config-server url in department-service so that when we start department-service it will load configuration file from config-server
        Hence add below properties in application.properties file
            spring.config.import=optional:configserver:http://localhost:8888



Step 5: Refactor Employee-Service to use Config-Server

    Add Config client and actuator dependecy in pom.xml of Employee-Service
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-actuator</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-config</artifactId>
                </dependency>

    Now Create Project(Service) specific configuration file in git repository.
        Give the file name should be same as service name like employee-service.properties
        and paste configure there.We can give file name in lower case as well.


    Now comment all the properties in application.properties file except
            spring.application.name= EMPLOYEE-SERVICE
        Because employee-service uses this application name to load the configuarion file from config-server


    Now need to configure config-server url in employee-service so that when we start employee-service it will load configuration file from config-server
        Hence add below properties in application.properties file
            spring.config.import=optional:configserver:http://localhost:8888

Step 6: Refresh Use Case- No Restart require after config changes

Use Case

![cs3](https://user-images.githubusercontent.com/42623098/234889383-59757b89-6fd3-4a27-853e-0b1c4d5040f0.jpg)


To test it.
Add a new properties in config git repository of department-service.properties file
    spring.boot.message=Hello, Department Service

Create a new Get REST API in department-service

            @RefreshScope // This anotation will force this spring bean to reload the configuration
            @RestController
            public class MessageController { 

                @Value("${spring.boot.message}")// To read value from configuration file i.e properties file
                private String message;

                @GetMapping("message")
                public String message(){
                    return message;
                }
            }

Manually call refresh end point of actuator in department-service

POST call
http://localhost:8080/actuator/refresh
        
#add this property in application.properties file of department-service
#It will enable all the endpoints of Actuators
#in order to enable /refresh endpoint in acyuator we need to add this property
management.endpoints.web.exposure.include=*

Now when we update configuration in git repository of department-service.properties file.
We only need to restart config-server to load latest properties from git repository to config-server.
No need to start microservices(here department-service) and changes will be refreshed in department-service using actuator refresh end point and using annotation @RefreshScope


#####################################################################################################

**Auto Refresh Config Changes using Spring Cloud Bus**

Problem with the Spring Cloud Config Server:
![cs4](https://user-images.githubusercontent.com/42623098/234889668-5663d6bc-239c-454d-b44e-0e0429ee1f4f.jpg)



Solution 

![cs5](https://user-images.githubusercontent.com/42623098/234889722-7aa5a202-c2ec-4d85-8033-37cb168addbb.jpg)


Development Steps


![cs6](https://user-images.githubusercontent.com/42623098/234889774-db755fdb-2c8e-4e36-8230-2eaaae889d7c.jpg)

![cs7](https://user-images.githubusercontent.com/42623098/234889884-536c5a1e-56e4-43b8-9af3-f572c0e208af.jpg)


Step 1: Add spring-cloud-starter-bus-amqp dependency yo department-service and employee-service
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
                </dependency>

Step 2: Install RabbitMQ using Docker
    install "Docker Desktop" 
    and go to docker hub and search rabbitmq to finc latest version of rabbitmq

    and come to docker desktop and run below command to pull docker image of rabbitmq to local
        docker pull rabbitmq:3.11.0

    Now lets start this docker image in docker container
        docker run --rm -it -p 5672:5672 rabbitmq:3.11.0
    This will start rabbitmq container on port 5672

Step 3: Add RabbitMQ configuration in application.properties of department-service and employee-service

        spring.rabbitmq.host=localhost
        spring.rabbitmq.port=5672
        spring.rabbitmq.username=guest
        spring.rabbitmq.password=guest

Step 4: Create simple REST API in employee-service

        Create MessageControl class in Controller of employee-service

            @RefreshScope // This anotation will force this spring bean to reload the configuration
            @RestController
            public class MessageController { 

                @Value("${spring.boot.message}")// To read value from configuration file
                private String message;

                @GetMapping("/users/message")
                public String message(){
                    return message;
                }
                
            }


Add spring.boot.message properties in employee-service.properties of git repository
        spring.boot.message=Hello, Employee Service

    Rerun department-service and employee-service

Step 5: Change department-service and employee-service properties file and call /busrefresh

    Updated spring.boot.message property in department-service.properties and employee-service.properties file

    Now call bus refresh actuator endpoint in any of the service
    Post request call http://localhost:8081/actuator/busrefresh
    it will take message broker i.e rabbitmq to brodcate  the configuration changes to respective microservices

    Now called message REST API in department-service and employee-service and we observed that 
    without restarting department service and employee-service we are getting the configuration changes

    ######################################################################################################################################################


**Distributed Tracing with Spring Cloud Sleuth and Zipkin**

Will See Later as it is depricated in spring version 3.0







##########################################################################################################################

**Circuit breaker using Resileence4J Implementation**


Reference Links Used in this Section 
Here are the links for your reference:
https://spring.io/projects/spring-cloud-circuitbreaker
https://resilience4j.readme.io/
https://resilience4j.readme.io/docs/circuitbreaker
https://resilience4j.readme.io/docs/retry


**What Problem Circuit Breaker Pattern Solves?**

The Circuit Breaker pattern is a popular design pattern used in Microservices Architecture, that falls under the Sustainable Design Patterns category. In Microservices architecture, a service usually calls other services to retrieve data, and there is the chance that the downstream service may be down. It may be cause by slow network connection, timeouts, or temporal unavailability. Therefore, retrying calls can solve the issue. However, if there is a severe issue on a particular microservice, then it will be unavailable for a longer time. In such case, the request will be continuously sent to that service, since the client doesn’t have any knowledge about a particular service being down. As a result, the network resources will be exhausted with low performance and bad user experience. Also, the failure of one service might lead to Cascading failures throughout the application.

Therefore, you can use the Circuit Breaker Design Pattern to overcome this problem.


Problem

Assume microservice call chain and if microservice M4 is down for some reason
 client -> M1 -> M2 -> M3 -> M4

Below Approach can provide the solution for this problem

1. Fallback method mechanism: Whenever microservice M4 goes down then microservice M3 will return some default response to microservice M2. In this way we can avoid cascade failure

2.  Circuit breaker pattern: When microservice M4 goes down then it won't allow M3 to hit M4 continuously. This approach will Save lots of resources and avoid continuous calls from microservice M3 to M4.

3. Retry mechanism: if microservice M4 is temporarly down , then we can implement retry mechanism.
example Retry 5 means M3 will try to communicate with M4 5 times. if M4 up by then it will get success response.

4. Rate Limiter: This pattern will limit number of calls from microservice M3 to M4




Circuit Breaker Design Pattern:


Therefore, you can use the Circuit Breaker Design Pattern to overcome this problem. With the help of this pattern, the client will invoke a remote service through a proxy. This proxy will basically behave as an electrical circuit breaker. So, when the number of failures crosses the threshold number, the circuit breaker trips for a particular time period. Then, all the attempts to invoke the remote service will fail within this timeout period. After the timeout expires, the circuit breaker allows a limited number of test requests to pass through it. If those requests succeed, the circuit breaker resumes back to the normal operation. Otherwise, if there is a failure, the timeout period begins again.

![c1](https://user-images.githubusercontent.com/42623098/235362847-4288162f-bc2f-4613-b639-88dc9d1fe900.jpg)

The Circuit Breaker Design pattern have three states:

1. Closed
2. Open
3. Half-Open



![c2](https://user-images.githubusercontent.com/42623098/235362867-e9243e2c-8572-4f2f-8a37-b9b4154a3e29.jpg)



Development Steps

![c3](https://user-images.githubusercontent.com/42623098/235362884-32d48722-351d-4b30-b340-5c29c2be3537.jpg)




Step 1: Add all the require dependency
        Add into employee-service
    <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
    </dependency>

    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>


Step 2: Using @CircuitBreaker annotation to a method(It is internally calling external service)
    Annotate getEmployeeById() method in EmployeeServiceImp  by below annotation
        @CircuitBreaker(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")


Step 3: Implement fallback method i.e getDefaultDepartment here
        implement it in EMployeeServiceImpl class

            //Implement fallback method i.e getDefaultDepartment

            public APIResponseDto getDefaultDepartment(Long employeeId){
                Employee employee = employeeRepository.findById(employeeId).get();

            DepartmentDto departmentDto = new DepartmentDto();
            //set default value to object departmentDto
            departmentDto.setDepartmentName("R&D Department");
            departmentDto.setDepartmentCode("RD001");
            departmentDto.setDepartmentDescription("Research and Development Department");

            //Convert Employee JPA Entity to EmployeeDto

            EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode()
            );
                
            
            APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto);
            return apiResponseDto;

            }

Step 4: Add circuit breaker configuration in application.properties file

            #Actuator endpoints for Circuit Breaker
            management.health.circuitbreaker.enabled=true
            management.endpoints.web.exposure.include=health
            management.endpoint.health.show-details=always

            #Circuilt Breaker Configuration
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.registerHealthIndicator=true
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.failureRateThreshold=50
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.minimumNumberOfCalls=5
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.automaticTransitionFromOpenToHalfOpenEnabled=true
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.waitDurationInOpenState=5s
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.permittedNumberOfCallsInHalfOpenState=3
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.slidingWindowSize=10
            resilience4j.circuitbreaker.instance.EMPLOYEE-SERVICE.slidingWindowType=COUNT_BASED

Step 5: Restart employee-service and test


**Retry Pattern implementation using Rejilience4j**

![c4](https://user-images.githubusercontent.com/42623098/235362911-a17a32ec-f3fc-4459-a939-4cf542911bb8.jpg)

Development Steps

![c5](https://user-images.githubusercontent.com/42623098/235362925-78262f52-59d0-42e7-9395-03df88349751.jpg)


Step 1: Using @Retry annotation to a method(it is calling to external service)

    comment @CircuitBreaker annotation
    //@CircuitBreaker(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
     Annotate getEmployeeById() method in EmployeeServiceImp  by below annotation
    @Retry(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")

Step 2: Fallback method implementation
    Already done

Step 3: Add Retry configuration in application.properties file
    #Retry Configuration
    #resilience4j.retry.instances.EMPLOYEE-SERVICE.registerHealthIndicator=true
    resilience4j.retry.instances.EMPLOYEE-SERVICE.maxAttempts=5
    resilience4j.retry.instances.EMPLOYEE-SERVICE.waitDuration=1s

Add loggers to check logs and validate

        private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

        //Check if department-service is down then it logs 5 times means trying to call getEmployeeById() method 5 times
        LOGGER.info("inside getEmployeeById() method");

        //and after 5 retry it will call fallback method i.e getDefaultDepartment()
        LOGGER.info("inside getDefaultDepartment() method");

###################################################################################################################

**Add New Organization Microservice to existing Project**

![d1](https://user-images.githubusercontent.com/42623098/235374748-4b45f428-f709-42ec-bef6-84bbbf8d5c57.jpg)

Development steps

![d2](https://user-images.githubusercontent.com/42623098/235374760-b3104d4b-6f86-44c2-a6f8-3f6ba30c9705.jpg)

Step 1: Create Organization Service using Spring Boot

![image](https://user-images.githubusercontent.com/42623098/235374771-2dc321d1-30b2-4089-8212-dde6cef54cbf.png)

Step 2: Configure MySQL
    Create database organization_db
    Add below properties in pom.xml
        spring.datasource.url=jdbc:mysql://localhost:3306/organization_db
        spring.datasource.username= root
        spring.datasource.password=Munnu@10Oct

        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
        spring.jpa.hibernate.ddl-auto=update

        server.port= 8083
        
Step 3 : Create Organization JPS Entity and Sping Data JPA Repository
        Entity
                @Getter
                @Setter
                @AllArgsConstructor
                @NoArgsConstructor
                @Entity
                @Table(name = "organizations")
                public class Organization {
                    @Id
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    private Long id;
                    @Column(nullable = false)
                    private String organizationName;
                    private String organizationDescription;
                    @Column(nullable = false, unique = true)
                    private String organizationCode;
                    @CreationTimestamp
                    private LocalDateTime createdDate;
                }

    Repository

        public interface OrganizationRepository extends JpaRepository<Organization, Long> {
            
        }

Step 4: Create OrganizationDto and OrganizationMapper

Step 5: Build save Organization REST API
Step 6: Build get Organization By organization code REST API
step 7: Make REST API call from Employee-Service to Organization-service
    Requirement
    
![d3](https://user-images.githubusercontent.com/42623098/235374834-39034b1a-dc98-45a4-9a26-f663c85dcb2c.jpg)


    Add an attribute organizationCode in Employee JPA Entity
    update EmployeeDto class
    update EmployeeMapper class
    Finally refactor EmployeeServiceImpl class and call get Organization REST API from Employee-Service
    Test

Step 8: Register Organization-Service as Eureka Client
        Add spring cloud dependency 
        Add Eureka Client dependency
        set application name(Spring.application.name=ORGANIZATION-SERVICE)

Step 9 : Refactor Organization-service to use Config-Server
    Add dependency
        <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-config</artifactId>
         </dependency>

    create organization-service.properties file in centralized git repository and pate all the properties except application name

    Configure config-server url in organization-service
        spring.config.import=optional:configserver:http://localhost:8888
    Rerun config-server and organization-service to reflect changes

![image](https://user-images.githubusercontent.com/42623098/235374883-1da1f04f-b429-42e6-af13-e39037ce02a0.png)


Step 10: Configure Spring Cloud Bus
        Add bus dependency
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
                </dependency>
        Need to add Actuator dependency
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-actuator</artifactId>
                </dependency>
        Next we need to add rabbitmq configuration in application.properties file
        By default, Spring Cloud bus uses rannitmq as message broker to broadcaste the events
                spring.rabbitmq.host=localhost
                spring.rabbitmq.port=5672
                spring.rabbitmq.username=guest
                spring.rabbitmq.password=guest

        Next, We need to enable spring boot actuator endpoints. We need to call /busrefresh actuator need point 
                management.endpoints.web.exposure.include=*

Step 11: Configure Routes for Orgnization-Service in API-Gateway

        Add below properties in API-Gateway to configure routes for organization-service
            #Rouest for Organization Service
            spring.cloud.gateway.routes[2].id=ORGANIZATION-SERVICE
            spring.cloud.gateway.routes[2].uri=lb://ORGANIZATION-SERVICE
            spring.cloud.gateway.routes[2].predicates[0]=Path=/api/organizations/**

        Add below property in organization-service
            eureka.instance.prefer-ip-address=true
        Rerun API-Gateway and organizatyion-service to reflect changes



###################################################################################################################

**Creating React Frontend Microservice**

![Re1](https://user-images.githubusercontent.com/42623098/235428690-476f1399-11d8-4595-933b-26bcb3a11c48.jpg)

React is javascript library used to develop user interfaces. It is developed by Facebook
React is used to develop single page application
React don't have ability to make Http Request. In order to make Http Request in React application,
We have to use a third party Http library i.e (axios library)


Development Steps

![Re2](https://user-images.githubusercontent.com/42623098/235428720-86c3dad0-3107-48ab-8fc1-03ef5941118f.jpg)

Step 1: Create React App using Create React App tool

        npx create-react-app react-frontend
        cd react-frontend
        npm start


        commands 

         npm start
            Starts the development server.

            npm run build
                Bundles the app into static files for production.

            npm test
                Starts the test runner.

            npm run eject
                Removes this tool and copies build dependencies, configuration files
                and scripts into the app directory. If you do this, you can’t go back!

Step 2:Adding Bootstrap in React using NPM

    npm install bootstrap --save

    Add below in index.js file
    import 'bootstrap/dist/css/bootstrap.min.css';

Step 3: Connecting React App with API-Gateway(REST API Call)
    We will use axios third party library to call Rest API

    install axios library. Below command will install axios in node_module folder. Also make entry in package.json file
        npm install axios --save

    create service folder in src. in service folder create EmployeeService.js
            import axios from 'axios'
            const EMPLOYEE_SERVICE_BASE_URL = "https://localhost:9191/api/employees";
            const EMPLOYEE_ID = 2;
            class EmployeeService{
                getEmployee(){
                    return axios.get(EMPLOYEE_SERVICE_BASE_URL + '/' + EMPLOYEE_ID);
                }
            }
            export default new EmployeeService

Step 4: Develop a React Component to Display user, Department and Organization Details
    create folder component in src folder. Create EmployeeComponent.js in component folder

    install Reactjs code snippets extension in vs code
    open EmployeeComponent.js and type rcc . It will generate react class component skelton

        import React, { Component } from 'react';
        import EmployeeService from '../service/EmployeeService';

        class EmployeeComponent extends Component {

            constructor(props) {
                super(props);
                this.state = {
                    employee: {},
                    department: {},
                    organization: {}
                }
            }
            componentDidMount(){
                EmployeeService.getEmployee().then((response) =>{
                    this.setState({employee : response.data.employee})
                    this.setState({department : response.data.department})
                    this.setState({organization : response.data.organization})
                    console.log(this.state.employee)
                    console.log(this.state.department)
                    console.log(this.state.organization)
                })
            }
            
            render() {
                return (
                    <div>
                        
                    </div>
                );
            }
        }

        export default EmployeeComponent;

Step 5: Design the page to Display User, Department and Organization Details
        implement render() method and componentDidMount() method in EmployeeComponent.js

######################################################################################################

**Generate Rest API Documentation for Department-Service**
REST API Documentation for Department Service Using SpringDoc Open API library
Development Steps


Strp 1: Adding springdoc-openapi Maven dependency
        <!-- https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.1.0</version>
        </dependency>

    http://localhost:8082/swagger-ui/index.html

    To get in json format

    http://localhost:8082/v3/api-docs

Step 2: Define General API Information using Annotation
    Annotate depart-service entry point class with below

            @OpenAPIDefinition(
            info = @Info(
                title = "Department-Service REST API",
                description = "Department-Service REST API Documentation",
                version = "v1.0",
                contact = @Contact(
                    name = "Pankaj",
                    email = "pankajdets@gmail.com",
                    url = "https://linkedin.com/in/pankajdets"
                ),
                license = @License(
                    name = "Apache 2.0",
                    url = "https://linkedin.com/in/pankajdets"
                )
            ),
            externalDocs = @ExternalDocumentation(
                description = "Department-Service Doc",
                url = "https://linkedin.com/in/pankajdets"
            )
        )

Step 3: Customizing Swagger API Documentaion using Annotation

    Annotate Controller class with 
        @Tag(
                name = "Department Service - DepartmentController",
                description = "Department Controller Exposes REST API for Department-Service"
        )

    Annotate saveDepartment() method with below
        @Operation(
        summary = "save Department REST API",
        description = "save Department REST API is used to save department object in a database"
        )
        @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
        )

    Annotate getDepartment() method with below
        @Operation(
        summary = "get Department REST API",
        description = "Get Department REST API is used to get department object from the database"
        )
        @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
        )

Step 4: Customizing Swagger Models Documentation with Annotations(DepartmentDto)
    Annotate DepartmentDto class with
        @Schema(
            description = "DepartmentDto Model Information"
        )
    
    Annotate DepartmentDto class member with
         @Schema(description = "Department Name")
         @Schema(description = "Department Description")
         @Schema(description = "Department Code")


#########################################################################################################
**Organization-Service REST API Documentation**
    implement same way as Department-Service

**Employee-Service REST API Documentation**
    implement same way as Department-Service



##############################################################################################################
**Dockering Spring Boot Application Step by Step**

![do1](https://user-images.githubusercontent.com/42623098/235493051-ee29cd84-981f-4602-8e33-62028d8a9fd4.jpg)

Dockerfile: It a text file contains all the instructions to build docker image
Docker Container: Runnning instance of Docker image
Docker Hub: Online Cloud Repository. Anybody from anywhere can pull the docker image from docker hub and run it

![do2](https://user-images.githubusercontent.com/42623098/235493074-4f0104c7-6a21-4f2d-a2ab-d94b563495ad.jpg)


Step 1: Create basic Spring boot Application

Step 2: Create Dockerfile in root directory of Spring Boot Project

        FROM eclipse-temurin:17

        //Meta Deta
        LABEL mentainer = "pankajdets@gmail.com"

        //whenever we run the container. app directory wii be created in the container
        WORKDIR /app

        //copy this jar file to app folder in the container and rename the jar filr to springboot-docker-demo.jar
        COPY target/jarfilename.jar  /app/springboot-docker-demo.jar

        //entrypoint to run the jar file 
        ENTRYPOINT["java", "-jar", "springboot-docker-demo.jar"]


Step 3: Build image using Dockerfile
    docker build -t springboot-docker-demo .
    docker build -t docker-image-name Dockerfile-location

    Here -t is used to tag image name "springboot-docker-demo" and . represent present working directory

    This command will execute step by step instructions writtern in Dockerfile and build image and store into local machine

    To check all the docker images in local machine hit
    docker images

    By default image will take latest tag
    To give tag (0.1.RELEASE) to docker image run 

    docker build -t springboot-docker-demo:0.1.RELEASE  .


Step 4: Run docker image in a docker container
    docker run -p 8080:8080 springboot-docker-demo
    docker run -p hostOperatingSystemPort : containerPort imageName

    -p to map port(mapping container port with host operating system port)

    Now docker image is running in doker container
    Inodert to access we need host Operating System port
    http://localhost:8080


    if we run command docker run -p 8081:8080 springboot-docker-demo
    menas we are mapping container port 8080 to host operating system port 8081 hence we can access it at 
    http://localhost:8081


        To check running containers hit
        docker ps 

        To run docker container in deatched mode(means docker container will run in background)- It will give dockerId
        docker run -p 8081:8080 -d springboot-docker-demo

        To get logs of docker container
        docker logs -f dockerId

        To stop running docker container
        docker stop dockerId

Step 5: Push Docker Image to DockerHub
    Create Account in DockerHub

    Inorder to push docker image to dockerHub we first need to login to dockerHub through terminal

    docker login

    it will ask username and password. provide dockerHub username and password
    once login succeeded

    Command to tag local docker image to repository in docker hub
    docker tag springboot-docker-demo pankajdets/springboot-docker-demo:0.1.RELEASE

    docker images
    docker push panksjdets/springboot-docker-demo: 0.1.RELEASE

    To remove doker image
    docker rmi imageId

Step 6: Pull Docker Image from DockerHub and run in docker container
    docker pull pankajdets/springboot-docker-demo:0.1.RELEASE

    docker run -p 3307:3306 --name localhost -e MYSQL_ROOT_PASSWORD=Munnu@10Oct -e MYSQL_DATABASE=employee_db
    -e MYSQL_USER=root  -d mysql:latest

    run in deatahed mode

    docker exec -it localhost bash

    in bash
    mysql -u root -p
        show databases;

#####################################################################################################################
**Dockerizing Spring Boot MySQl Application Using Docker Network**
Dockerizing springboot-restful-webservices-using-Dto application. It is using MySql database

![do3](https://user-images.githubusercontent.com/42623098/235493235-e637f5ab-061a-4dd0-96b3-973c560fe288.jpg)


Pull mysql image from docker hub
    docker pull mysql
    docker images

Create docker Network 
    we want my springboot application container to communicate with mysql container so that we need to deploy both in same docker Network

    docker network create springboot-mysql-net

    To list out all the docker network
    docker network ls

    In bridge type of network two container can communicate with each other

Run mysql docker image in docker container
    docker run --name mysqldb --network springboot-mysql-net -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABAE=employee_db -d mysql

    To see all running container
    docker ps

    docker exec -it containerID bash

    in bash
    mysql -u root -p
    enter root password and it will open mysql command line

Create Dockerfile in springbootb application root folder and Build image 
first generate jar file
    mvn clean package 


Dockerfile

        FROM eclipse-temurin:17

        //Meta Deta
        LABEL mentainer = "pankajdets@gmail.com"

        //whenever we run the container. app directory wii be created in the container
        WORKDIR /app

        //copy this jar file to app folder in the container and rename the jar filr to springboot-docker-demo.jar
        COPY target/jarfilename.jar  /app/springboot-docker-demo.jar

        //entrypoint to run the jar file 
        ENTRYPOINT["java", "-jar", "springboot-docker-demo.jar"]

active docker profile in springboot application
    Add below property in application.properties file
            spring.profiles.active=docker

create new properties file for docker profiling 
file name: application-docker.properties
            #JBC url to connect with url
            #employee_db is database name which we have already in mysqldb docker container
            #we have already created docker container with name mysqldb and root password is set to root
            spring.datasource.url=jdbc:mysql://mysqldb:3306/employee_db
            spring.datasource.username=root
            spring.datasource.password=root

            #Hibernare Dialect: Hibernate will use this dialect to create appropriate SQL statement with respect to database
            spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
            #This property tells Hibernate that create table automatically if tables are not exists. I
            #if tables are already in the database then change or alter the table as per the jpa entity mapping changes 
            spring.jpa.hibernate.ddl-auto=update



            # *will enable all the actuator endpoints
            management.endpoints.web.exposure.include=*
            management.info.env.enabled=true
            management.endpoint.health.show-details=always
            Management.endpoint.shutdown.enabled=true

            info.app.name=Spring Boot Restful Web Services
            info.app.description=Spring Boot Restful Web Service Demo
            info.app.version=1.0.0

build docker image 
    docker build -t pringboot-restful-webservices .
    docker images


Run Spring Boot App Docker Image in a  container and Test CRUD APIs

    docker run --network springboot-mysql-net --name springboot-mysql-container -p 8080:8080 springboot-restful-webservices


To run in deatached mode

    docker run --network springboot-mysql-net --name springboot-mysql-container -p 8080:8080 -d springboot-restful-webservices


To see logs
    docker logs -f containerID

Now Test RESTAPI using postman client

############################################################################################################################
**Dockerizing Spring Boot MySQL Application Using Docker Compose**

Tool to defining and starting multi container docker application

Using single command we can start all the services

![do4](https://user-images.githubusercontent.com/42623098/235493326-c5a30904-f2ab-4308-aa0b-8910bb1bc95d.jpg)


docker-compose.yml

    version: "3.8"
    services:
    mysqldb:
        container_name: mysqldb
        image: mysql
        environment:
        MYSQL_ROOT_PASSWORD: root
        MYSQL_DATABASE: employeedb
        networks:
        springboot-mysql-net:
    springboot-restful-webservices: 
        container_name: springboot-restful-webservices
        build:
        context: .
        dockerfile: Dockerfile
        ports:
        - "8080:8080"
        depends_on:
        - mysqldb
        networks:
        springboot-mysql-net:
        restart: on-failure
        
    networks:
    springboot-mysql-net:



run command: docker-compose up -d --build


Test REST API and logs


#########################################################################################################################################
