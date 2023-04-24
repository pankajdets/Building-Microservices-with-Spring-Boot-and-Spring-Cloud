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



