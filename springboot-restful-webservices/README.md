** Spring Boot Restful Webservices(CRUD Operaion) using MySQL**

![image](https://user-images.githubusercontent.com/42623098/233791310-17476758-288f-474f-aed4-ea9cc9a8308b.png)



Added Below dependency


**Configure MySQL database in Springboot application**
step 0: Add MySQL Driver dependency in pom.xml
step1: create database in MySQL
        create database user_management;

step3: Add below properties in application.properties file

        #JBC url to connect with url
        #user_management is database name which we have already created in MySQL
        spring.datasource.url=jdbc:mysql://localhost:3306/user_management
        spring.datasource.username=root
        spring.datasource.password=Munnu@10Oct

        #Hibernare Dialect: Hibernate will use this dialect to create appropriate SQL statement with respect to database
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
        #This property tells Hibernate that create table automatically if tables are not exists. I
        #if tables are already in the database then change or alter the table as per the jpa entity mapping changes 
        spring.jpa.hibernate.ddl-auto=update


**Creating User JPA Entity**

        package com.pankajdets.springootrestfulwebservices.model;

        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.Table;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        @Getter //lombok dependency
        @Setter  //lombok dependency
        @NoArgsConstructor  //lombok dependency
        @AllArgsConstructor  //lombok dependency
        @Entity  // Specifies that this class is a JPA Entity
        @Table(name = "users")   //To configure table details. By Defaukt ist will take table name as class name
        public class User {
            @Id //to make it primary key
            @GeneratedValue(strategy = GenerationType.IDENTITY) // This internally uses auto increment feature to increment Id
            private long id;
            @Column(nullable = false)  // To customize column. If column name has two word then JPA automatically convert it to "first_name"
            private String firstName;
            @Column(nullable = false)
            private String lastName;
            @Column(nullable = false, unique = true)
            private String email;
        }

Now after running application users table is created in MySQL user_management database

**Create UserRepository which extends JpaRepository*


        public interface UserRepository extends JpaRepository<User, Long> {
            
        }
        
        
![image](https://user-images.githubusercontent.com/42623098/233791349-e95d7774-13ee-42c9-b494-a8b082e800b7.png)
        
        

**Build Create User REST API**
**Service**
Create Service package and create UserService inteface

        public interface UserService {
            User createUser(User user);
            
        }

Create Impl package
And Create class UserServiceImpl which implements  UserService inface methods

        @Service
        @AllArgsConstructor //to create constructor  for userRepository instance variable 
        public class UserServiceImpl implements UserService {

            private UserRepository userRepository;// Constructor based dependency injection to inject userRepository in UserServiceImpl class
            //typically we need to @Autowired annotation to inject the dependency
            // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
            // Here we have UserServiceImpl is spring bean having single parameterize constructor for userRepository
            @Override
            public User createUser(User user) {
                
                return userRepository.save(user);
            }
            
        }

**Controller**
Create Controller package and then create UserController class

        @RestController // To this class spring MVC rest controller
        @AllArgsConstructor //to create constructor  for single argument  userService instance variable
        @RequestMapping("api/users") // satting base url at class level
        public class UserController {
            private UserService userService;// Constructor based dependency injection to inject userService in UserController class
            //typically we need to @Autowired annotation to inject the dependency
            // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
            // Here using @AllArgsConstructor we have created Constructor for single argument userService 
            
            //build Create User REST API
            @PostMapping
            public ResponseEntity<User> createUser(@RequestBody User user){
                User savedUser = userService.createUser(user);
                return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
            }

            
        }

**Build Get User By ID REST API**
**Service**
    Add User getUserById(Long userId);  in UserService interface
    Implement getUserById(Long userId) method in UserServiceImpl class

        @Override
        public User getUserById(Long userId) {
            Optional<User> optionalUser =userRepository.findById(userId);
            return optionalUser.get();
        }

**Controller**

    //build get user by id REST API
    //http://localhost:8080/api/users/1
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId){
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


**Buid Get All User REST API**
**Service**
    Add List<User> getAllUsers();  in UserService interface
    Implement  getAllUsers()method in UserServiceImpl class

        @Override
        public List<User> getAllUsers() {
            List<User> users = userRepository.findAll();
            return users;
        }
**Controller**

    //build get all users REST API
    //http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllusers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

** BUILD UPDATE USER REST API**
**Service**
    Add User updateUser(User user);  in UserService interface
    Implement updateUser(User user) method in UserServiceImpl class

        @Override
        public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail();
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
        }


**Controller**

            //build update user REST API
            //http://localhost:8080/api/users/1
            @PutMapping("{id}")
            public ResponseEntity<User> updateUSer(@PathVariable("{id}") Long userId, @RequestBody User user){
                user.setId(userId);
                User updatedUser = userService.updateUser(user);
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            }


**Build Delete User REST API**

**Service**
    Add void deleteUser(Long userId);  in UserService interface
    Implement deleteUser(Long userId) method in UserServiceImpl class

        @Override
        public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        }

**Controller**

    //build delete user REST API
    //http://localhost:8080/api/users/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteuser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<String>("user Successfully deleted", HttpStatus.OK);
    }

    

##################################################################################################################################################################
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
