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


**Configure And Develop REST APIs in employee-service**

Setup Database connection in employee-service
    Create database employee_db
    Add properties in application.properties file

Create Employee JPA Entity and Spring Data JPA Repository
Build Save Employee REST API in employee-service
Build Get Employee REST API in employee-service

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





##########################################################################################################################################

    




