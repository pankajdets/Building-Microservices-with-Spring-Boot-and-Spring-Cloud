**In previous project "Springboot-restful-webservices" we have implemented CRUD operation
for User**

But Problem with previous project is that we are returing JPA Entity to Client which is not safe
there may be some sensitive information in JPA Entity which we are sending to client hence there is a chance that client can access sensetive information like password

**We can Overcome this Problem using Data Transfer Object (DTO) Pattern**

Data transfer Object(DTO) Pattern is  widely used design pattern to transfer the data between client and server

**Main Advantage of this pattern is :** 
1. To reduce number of remote calls
2. server use DTO to send only the required data to the client


DTOs or Data Transfer Objects are objects that carry data between processes in order to reduce the number of methods calls. The pattern was first introduced by Martin Fowler in his book EAA.

Fowler explained that the pattern's main purpose is to reduce roundtrips to the server by batching up multiple parameters in a single call. This reduces the network overhead in such remote operations.

Another benefit is the encapsulation of the serialization's logic (the mechanism that translates the object structure and data to a specific format that can be stored and transferred). It provides a single point of change in the serialization nuances. It also decouples the domain models from the presentation layer, allowing both to change independently.

DTOs normally are created as POJOs. They are flat data structures that contain no business logic. They only contain storage, accessors and eventually methods related to serialization or parsing.
The data is mapped from the domain models to the DTOs, normally through a mapper component in the presentation or facade layer.

![image](https://user-images.githubusercontent.com/42623098/233796989-0ebed01b-0e71-489d-a125-c6168fcac0d5.png)


4. When to Use It?
DTOs come in handy in systems with remote calls, as they help to reduce the number of them.

DTOs also help when the domain model is composed of many different objects and the presentation model needs all their data at once, or they can even reduce roundtrip between client and server.

With DTOs, we can build different views from our domain models, allowing us to create other representations of the same domain but optimizing them to the clients' needs without affecting our domain design. Such flexibility is a powerful tool to solve complex problems.


**Development Steps for Usecase**
1. Create UserDto Class
2. Refactor Create User REST API to use DTO
3. Create UserMapper class
4. Refactor Get User By Id REST API to use DTO
5. Refactor Get All Users REST API to use DTO
6. Refactor Update User REST API to use DTO
7. Refactor Delete User REST API to use DTO

**1.Create UserDto Class**
    Create dto package
    Create UserDto class
        //Don't include sensitive information in User DTO
        //Beacuse We don't want to send sensitive information to client
        @Setter
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public class UserDto {
            private Long id;
            private String firstName;
            private String lastName;
            private String email;
        }

**2. Refactor Create User REST API to use DTO**

**Service**

Change function declarion in UserService interface

        UserDto createUser(UserDto userDto);

Update createUser() method in UserServiceImpl class

        @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                User user = new User(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail()  
                );

                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                UserDto savedUserDto = new UserDto(
                    savedUser.getId(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    savedUser.getEmail()
                    );
                return savedUserDto;       
            }

**Controller**
Update createUser POST API in controller

    //build Create User REST API
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto savedUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }


**Create UserMapper Class**
//To conver User JPA Entity to UserDto and vice versa
Create Mapper directoty
Create UserMapper class and and two static methods 1. UserDto mapToUserDto(User user) 2. User mapToUser(UserDto userDto)

        public class UserMapper {

            //Convert User JPA Entity to UserDto
            public static UserDto mapToUserDto(User user){
                UserDto userDto = new UserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()
                    );
                return userDto;
            }

            // convert UserDto to User JPA Entity
            public static User mapToUser(UserDto userDto){
                User user = new User(
                    userDto.getId(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail()
                );

                return user;
            }
            
        }

Now update createUser() in UserServiceImpl class as below to User mapper methods

        @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                User user = UserMapper.mapToUser(userDto);

                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
                return savedUserDto;       
            }


**Refactor Get User By Id REST API to use DTO**

**Service**
update method prototype in UserService interface
        UserDto getUserById(Long userId);
update getUserById() method definition in UserServiceImpl class
        @Override
        public UserDto getUserById(Long userId) {
            Optional<User> optionalUser =userRepository.findById(userId);

            User user = optionalUser.get();
            return UserMapper.mapToUserDto(user);
        }

**Controller**
update getUserById() in UserController class

            //build get user by id REST API
            //http://localhost:8080/api/users/1
            @GetMapping("{id}")
            public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
                UserDto userDto = userService.getUserById(userId);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            }

**Refactor Get ALL User REST API to use DTO**
**Service**
update prototype of method getAllUsers() in UserService interface
        List<UserDto> getAllUsers();
update function defenition of getAllUsers() in UserServiceImpl class
            @Override
            public List<UserDto> getAllUsers() {
                List<User> users = userRepository.findAll();
                return users.stream().map(UserMapper :: mapToUserDto).collect(Collectors.toList());
            }

**Controller**
            //build get all users REST API
            //http://localhost:8080/api/users
            @GetMapping
            public ResponseEntity<List<UserDto>> getAllusers(){
                List<UserDto> usersDto = userService.getAllUsers();
                return new ResponseEntity<>(usersDto, HttpStatus.OK);
            }

**Refactor Update User REST API to use DTO**
**Service**
update function prototype in userService interface
        UserDto updateUser(UserDto userDto);
update function definition in userServiceImpl class
        @Override
        public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId()).get();
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.mapToUserDto(updatedUser);
        }
**Controller**

        //build update user REST API
        //http://localhost:8080/api/users/1
        @PutMapping("{id}")
        public ResponseEntity<UserDto> updateUSer(@PathVariable("id") Long userId, @RequestBody UserDto userDto){
            userDto.setId(userId);
            UserDto updatedUserDto = userService.updateUser(userDto);
            return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
        }


**Refactor Delete User REST API to use DTO**

Haven't use User JPA Entity object hence no need to refector





**We have built UserMapper class to Map User to UserDto and Vice versa manually**
This is not a good practice to convert convert User JPA Entity to UserDto and UserDto to JPA Entity manually

        public class UserMapper {

            //Convert User JPA Entity to UserDto
            public static UserDto mapToUserDto(User user){
                UserDto userDto = new UserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()
                    );
                return userDto;
            }

            // convert UserDto to User JPA Entity
            public static User mapToUser(UserDto userDto){
                User user = new User(
                    userDto.getId(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail()
                );

                return user;
            }
            
        }


**Using Mapping libraries to Map Entity to DTO and vice versa**

**1. modelmapper library**

**2. mapStruct library**

**1. Using ModelMapper library to map Entity to DTO and vice versa**

step 1: Add ModelMapper dependency in pom.xml
        <!-- https://mvnrepository.com/artifact/org.modelmapper/modelmapper -->
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.1.1</version>
        </dependency>
Step 2: Configure ModelMapper class as spring bean
 Instead of creating ModelMapper object using new keyword, We can create ModelMapper clas as spring bean so that we can inject MaperMapper spring bean anywhere in the application and can use its methods

 
        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }

Step 3: Inject and use ModelMapper Spring bean in Service class

    private ModelMapper modelMapper; 
    // Constructor based dependency injection to inject ModelMapper object  in UserServiceImpl class
    //@Autowired is not required as there is single parameterize constructor @AllArgsConstructor 

    Refactor UserServiceImpl class methods to use ModelMapper instance

        @Service
        @AllArgsConstructor //to create constructor  for userRepository instance variable 
        public class UserServiceImpl implements UserService {

            private UserRepository userRepository;// Constructor based dependency injection to inject userRepository in UserServiceImpl class
            //typically we need to @Autowired annotation to inject the dependency
            // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
            // Here we have UserServiceImpl is spring bean having single parameterize constructor for userRepository
            
            private ModelMapper modelMapper; // Constructor based dependency injection to inject ModelMapper object  in UserServiceImpl class
            //@Autowired is not required as there is single parameterize constructor @AllArgsConstructor 

            @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                //User user = UserMapper.mapToUser(userDto);

                User user = modelMapper.map(userDto, User.class);
                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                //UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);

                UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);
                return savedUserDto;       
            }

            @Override
            public UserDto getUserById(Long userId) {
                Optional<User> optionalUser =userRepository.findById(userId);

                User user = optionalUser.get();
                //return UserMapper.mapToUserDto(user);
                return modelMapper.map(user, UserDto.class);
            }

            @Override
            public List<UserDto> getAllUsers() {
                List<User> users = userRepository.findAll();
                //return users.stream().map(UserMapper :: mapToUserDto).collect(Collectors.toList());

                return users.stream().map((user)->modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
            }

            @Override
            public UserDto updateUser(UserDto userDto) {
            User existingUser = userRepository.findById(userDto.getId()).get();
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());
            User updatedUser = userRepository.save(existingUser);
            //return UserMapper.mapToUserDto(updatedUser);

            return modelMapper.map(updatedUser, UserDto.class);
            }

            @Override
            public void deleteUser(Long userId) {
            userRepository.deleteById(userId);
            }

            
            
        }



**2. Using MapStruct library to map Entity to DTO and vice versa**

step 1: Add MapStruct maven dependency in pom.xml file
       <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>

        <properties>
            <org.mapstruct.version>1.5.4.Final</org.mapstruct.version>
            <org.projectlombok.version>1.18.20</org.projectlombok.version>
            <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
        </properties>

        Now we need to add mapstruct processor plugin in pom.xml

        In order to support lombok library with mapstruct we need to use few more configuration

                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.1</version>
                    <configuration>
                        <source>17</source>
                        <target>17</target>
                        <!-- See https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html -->
                        <!-- Classpath elements to supply as annotation processor path. If specified, the compiler   -->
                        <!-- will detect annotation processors only in those classpath elements. If omitted, the     -->
                        <!-- default classpath is used to detect annotation processors. The detection itself depends -->
                        <!-- on the configuration of annotationProcessors.                                           -->
                        <!--                                                                                         -->
                        <!-- According to this documentation, the provided dependency processor is not considered!   -->
                        <annotationProcessorPaths>
                            <path>
                                <groupId>org.mapstruct</groupId>
                                <artifactId>mapstruct-processor</artifactId>
                                <version>${org.mapstruct.version}</version>
                            </path>
                            <path>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                                <version>${org.projectlombok.version}</version>
                            </path>
                            <path>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok-mapstruct-binding</artifactId>
                                <version>${lombok-mapstruct-binding.version}</version>
                            </path>
                        </annotationProcessorPaths>
                    </configuration>
                </plugin>


Step 2: Create UserMapper using MapStruct

    Create interface AutoUserMapper in mapper package and declare two method as below
        @Mapper
        public interface AutoUserMapper {
            //mapstruct will create implementation for these methods at compilation time
            //We don't have to implement these methods

            
            //It will provide Implementation of interfae AutoUserMapper at compilation time
            //We can use Mapper instance to call the mapping methods
            AutoUserMapper Mapper = Mappers.getMapper(AutoUserMapper.class);    

            //Sometime fields name are fifferent in JPA Entity and DTO in that situation we can use
            //We can use @Mapping annotation having source=JPA Entity attribute name" and target= "DTO attribute name"
            //@Mapping(source="email", target= "emailAddress" )


            UserDto mapToUserDto(User user);
            User mapToUser(UserDto userDto);
        }



step 3: User AutoUserMapper in UserServiceImpl class
    Refactor UserServiceImpl class

        @Service
        @AllArgsConstructor //to create constructor  for userRepository instance variable 
        public class UserServiceImpl implements UserService {

            private UserRepository userRepository;// Constructor based dependency injection to inject userRepository in UserServiceImpl class
            //typically we need to @Autowired annotation to inject the dependency
            // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
            // Here we have UserServiceImpl is spring bean having single parameterize constructor for userRepository
            
            private ModelMapper modelMapper; // Constructor based dependency injection to inject ModelMapper object  in UserServiceImpl class
            //@Autowired is not required as there is single parameterize constructor @AllArgsConstructor 

            @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                //User user = UserMapper.mapToUser(userDto);
                //User user = modelMapper.map(userDto, User.class);

                User user = AutoUserMapper.MAPPER.mapToUser(userDto);
                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                //UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
                //UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);

                UserDto savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);

                return savedUserDto;       
            }

            @Override
            public UserDto getUserById(Long userId) {
                Optional<User> optionalUser =userRepository.findById(userId);

                User user = optionalUser.get();
                //return UserMapper.mapToUserDto(user);
                //return modelMapper.map(user, UserDto.class);
                return AutoUserMapper.MAPPER.mapToUserDto(user);
            }

            @Override
            public List<UserDto> getAllUsers() {
                List<User> users = userRepository.findAll();
                //return users.stream().map(UserMapper :: mapToUserDto).collect(Collectors.toList());
                //return users.stream().map((user)->modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
                return users.stream().map((user)->AutoUserMapper.MAPPER.mapToUserDto(user)).collect(Collectors.toList());
            }

            @Override
            public UserDto updateUser(UserDto userDto) {
            User existingUser = userRepository.findById(userDto.getId()).get();
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());
            User updatedUser = userRepository.save(existingUser);
            //return UserMapper.mapToUserDto(updatedUser);
            //return modelMapper.map(updatedUser, UserDto.class);
            return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
            }

            @Override
            public void deleteUser(Long userId) {
            userRepository.deleteById(userId);
            }

            
            
        }



                    **Exception Handling in Spring Boot Application**

**Spring Boot Default Error Handling Overview**

![IMG20230423155108](https://user-images.githubusercontent.com/42623098/233842045-e206d6dd-3515-42ee-872e-29e646905a5d.jpg)


**Spring Boot REST API Exception Handling Overview**

![IMG20230423155411](https://user-images.githubusercontent.com/42623098/233842283-f8ea6318-19ec-4b73-8b61-5e9ebb887589.jpg)


**Create And Use ResourceNotFound Custom Exception**
    **Requirement 1**
    
    
![IMG20230423155246](https://user-images.githubusercontent.com/42623098/233842218-da8a96b9-7add-40df-80d7-efc877dd886f.jpg)

    
    Development Steps
    
    
![IMG20230423155510](https://user-images.githubusercontent.com/42623098/233842380-18374805-c402-4747-a95d-fe4a79a83e7f.jpg)



    Create exception package
    Create ResourceNotFoundException.java class

            
        @ResponseStatus(value = HttpStatus.NOT_FOUND) // To return HttpStatus code
        public class ResourceNotFoundException extends RuntimeException{
            private String resourceName;
            private String fieldName;
            private Long fieldValue;

            public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue){
                super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
                this.resourceName = resourceName;
                this.fieldName = fieldName;
                this.fieldValue = fieldValue;
            }
        }

    Refactor UserServiceImpl methods to user Custom Exception (ResourceNotFoundException)

        @Service
        @AllArgsConstructor //to create constructor  for userRepository instance variable 
        public class UserServiceImpl implements UserService {

            private UserRepository userRepository;// Constructor based dependency injection to inject userRepository in UserServiceImpl class
            //typically we need to @Autowired annotation to inject the dependency
            // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
            // Here we have UserServiceImpl is spring bean having single parameterize constructor for userRepository
            
            private ModelMapper modelMapper; // Constructor based dependency injection to inject ModelMapper object  in UserServiceImpl class
            //@Autowired is not required as there is single parameterize constructor @AllArgsConstructor 

            @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                //User user = UserMapper.mapToUser(userDto);
                //User user = modelMapper.map(userDto, User.class);

                User user = AutoUserMapper.MAPPER.mapToUser(userDto);
                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                //UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
                //UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);

                UserDto savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);

                return savedUserDto;       
            }

            @Override
            public UserDto getUserById(Long userId) {
                User user = userRepository.findById(userId).orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId)
                );

                //return UserMapper.mapToUserDto(user);
                //return modelMapper.map(user, UserDto.class);
                return AutoUserMapper.MAPPER.mapToUserDto(user);
            }

            @Override
            public List<UserDto> getAllUsers() {
                List<User> users = userRepository.findAll();
                //return users.stream().map(UserMapper :: mapToUserDto).collect(Collectors.toList());
                //return users.stream().map((user)->modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
                return users.stream().map((user)->AutoUserMapper.MAPPER.mapToUserDto(user)).collect(Collectors.toList());
            }

            @Override
            public UserDto updateUser(UserDto userDto) {
            User existingUser = userRepository.findById(userDto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", userDto.getId())
            );
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());
            User updatedUser = userRepository.save(existingUser);
            //return UserMapper.mapToUserDto(updatedUser);
            //return modelMapper.map(updatedUser, UserDto.class);
            return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
            }

            @Override
            public void deleteUser(Long userId) {

                User existingUser = userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", userId)
            );

            userRepository.deleteById(userId);
            }

            
            
        }

We were using Spring Boot default exeception handler


**Create Error Details Class to hold the custom Error Response**
    Create ErrorDetails class in exception package

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class ErrorDetails {
            private LocalDateTime timestamp;
            private String message;
            private String path;
            private String errorCode;
            
        }
    
    Create GlobalExceptionHandler class to handle specific and global exceptions

    **At Controller level**

            @ExceptionHandler(ResourceNotFoundException.class) // To handle Specific Exception and return custom Error Response back to client
            public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "USER_NOT_FOUND"
            );

            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

Or we can create GlobalExceptionHandler class in exception package

         @ControllerAdvice //To handle the Exception Globally 
        //Means we use his annotation to handle all the specific exception and as well as global exception in single place
        public class GlobalExceptionHandler {

            @ExceptionHandler(ResourceNotFoundException.class) // To handle Specific Exception and return custom Error Response back to client
            public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "USER_NOT_FOUND"
            );

            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }
            
        }


**Requirement 2**
**If User email already exists in the database then we need to to throw exception with proper error message and status code**

![IMG20230423180021](https://user-images.githubusercontent.com/42623098/233842496-85d2f559-85bb-4590-9984-b20db7049012.jpg)



    Create  EmailAlreadyExistsException class by extending RuntimeException

        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public class EmailAlreadyExistsException extends RuntimeException {
            private String message;

            public EmailAlreadyExistsException(String message){
                super(message);
            }
            
        }

    Add query mehod proptotype in UserRepository to find User by email

        public interface UserRepository extends JpaRepository<User, Long> {
            
            Optional<User> findByEmail(String email);
        }

    
    Refactor createUser() method in UserServiceImpl class to throw exception if User email already exists in Database

            @Override
            public UserDto createUser(UserDto userDto) {
                //conver UserDto to User JPA Entity
                //Because we need to save user JPA Entity to database
                //User user = UserMapper.mapToUser(userDto);
                //User user = modelMapper.map(userDto, User.class);

                Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
                if(optionalUser.isPresent()){
                    throw new EmailAlreadyExistsException("Email Already Exists for User");
                }

                User user = AutoUserMapper.MAPPER.mapToUser(userDto);
                User savedUser = userRepository.save(user);

                //covert User JPA Entity savedUser to UserDto object
                //UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
                //UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);
                
                UserDto savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);

                return savedUserDto;       
            }

    Add handler method handleEmailAlreadyExistsException() in GlobalExceptionHandler class

        @ExceptionHandler(EmailAlreadyExistsException.class) // To handle Specific Exception and return custom Error Response back to client
        public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception,
                                                                            WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "USER_EMAIL_ALREADY_EXISTS"
            );

            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }


 **Spring Boot REST API Global Exception Handling**

    Add Global Exception Handler in GlobalExceptionHandler class. It will handle all other excpetion apart from that two custom exception

    
        //Global Exception Handler
        @ExceptionHandler(Exception.class) // To handle Specific Exception and return custom Error Response back to client
        public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                                            WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "INTERNAL SERVER ERROR"
            );

            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        

        **Spring Boot REST API Request Validation**

1. In Java, the Java Bean Validation API has become the de-facto standard for handling validfations in java projects.

2. Hibernate Validator is the reference implementation of the validation API.

![IMG20230423190657](https://user-images.githubusercontent.com/42623098/233845906-8c297bde-0ed0-4c62-ae98-8ef836e7aba1.jpg)


![IMG20230423190858](https://user-images.githubusercontent.com/42623098/233845976-527b8ad5-1ff9-4739-9628-69c3798b4831.jpg)
   



**Validate Create and Update User REST API Requests**

Development Steps

![IMG20230423191113](https://user-images.githubusercontent.com/42623098/233846051-7f39845a-e55b-4d4b-8721-e057642ef4ad.jpg)


    step 1: Add Dependency

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

    step 2: Add Validation Annotations to USerDto class

        //Don't include sensitive information in User DTO
        //Beacuse We don't want to send sensitive information to client
        @Setter
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public class UserDto {
            private long id;
            
            @NotEmpty //Requirement like User first name should not be null or empty
            private String firstName;
            @NotEmpty //Requirement like User last name should not be null or empty
            private String lastName;
            @NotEmpty //Requirement like User email name should not be null or empty
            @Email  //Requirement like email address should be valid
            private String email;
        }
    
    
    step 3: Enable Validation using @Valid annotation on Create and Update Rest APIs

            @PostMapping
            public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
                UserDto savedUserDto = userService.createUser(userDto);
                return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
            } 

            @PutMapping("{id}")
            public ResponseEntity<UserDto> updateUSer(@PathVariable("id") Long userId, @RequestBody @Valid UserDto userDto){
                userDto.setId(userId);
                UserDto updatedUserDto = userService.updateUser(userDto);
                return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
            }
    
    step 4: Customize validation Error Response and send back to client
        extend GlobalExceptionHandler from ResponseEntityExceptionHandler
        Ovverdide method handleMethodArgumentNotValid() as below

        @ControllerAdvice //To handle the Exception Globally 
        //Means we use his annotation to handle all the specific exception and as well as global exception in single place
        public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                HttpHeaders headers, HttpStatusCode status, WebRequest request) {
            
            Map<String, String> errors = new HashMap<>();
            List<ObjectError> errorList = ex.getBindingResult().getAllErrors();

            errorList.forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message );

            });
            
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);


    We can return customize message to client after adding message attribute to annotation

            public class UserDto {
            private long id;
            //Requirement like User first name should not be null or empty
            @NotEmpty(message = "User first name should not be null or empty") 
            private String firstName;
            //Requirement like User last name should not be null or empty
            @NotEmpty(message = " User last name should not be null or empty")
            private String lastName;
            //Requirement like User email name should not be null or empty
            //Requirement like email address should be valid
            @NotEmpty(message = " User email name should not be null or empty")
            @Email(message = " email address should be valid")
            private String email;
            }


        }
        }



                 **SpringBoot Actuator- Production Ready Features**



1. Spring Boot Actuator module provides productionready features such as monitoring, metrics and
health checks.
2. The Spring Boot Actuator enables you to monitor the application using HTTP endpoints and JMX.
3. Spring Boot Provides a spring-boot-starter-actuator library to auto-configure Actuator

[Spring+Boot+Actuator+Notes.pdf](https://github.com/pankajdets/Building-Microservices-with-Spring-Boot-and-Spring-Cloud/files/11304335/Spring%2BBoot%2BActuator%2BNotes.pdf)


    step 1: Add below Actuator Dependency in pom.xml

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

    Restart application. By default spring boot exposes only one endpoint i.e /actuator. We can enable all the endpoints exposer by actuator by adding property in application.properties file


    Step 2: Add below properties to expose all actuators endpoints
        # *will enable all the actuator endpoints(13 endpoints)
        management.endpoints.web.exposure.include=*
        

    **Commonly used Actuator endpoints**
    
        1. Actuator /info endpoint
        2. Actuator /health endpoint
        3. Actuator /beans endpoint
        4. Actuator /conditions endpoint
        5. Actuator /mappings endpoint
        6. Actuator /configprops endpoint
        7. Actuator /matrics endpoint
        8. Actuator /env endpoint
        9. Actuator /threaddump endpoint
        10.Actuator /loggers endpoint
        11.Actuator /shutdown Endpoint

    **1. The /info Endpoint**
        If you added any information about the application in
        application.properties then we can view it using /info
        endpoint:
        http://localhost:8080/actuator/info
        
        Add below information about application in application.properties file

            management.info.env.enabled=true

            info.app.name=Spring Boot Restful Web Services
            info.app.description=Spring Boot Restful Web Service Demo
            info.app.version=1.0.0


    **2 The /health Endpoint**
        The /health endpoint shows the health of the
        application, including the disk space, databases and
        more.
        http://localhost:8080/actuator/health

        By default actuator display only the status of the application. We can display other attributes by adding below property in application.properties file

        management.endpoint.health.show-details=always

    **3 The /beans Endpoint**
        The /beans endpoint shows all the beans registered in
        your application, including the beans you explicitly
        configured and those auto configured by Spring Boot.
        http://localhost:8080/actuator/beans

    **4 The /conditions Endpoint**
        The /conditions endpoint shows the auto
        configuration report, categorised into pasitiveMatches
        and negativeMatches
        http://localhost:8080/actuator/conditions


    **5 The /mappings Endpoint**
        The /mappings endpoint shows all the
        @RequestMapping paths declared in the application.
        This is very helpful for checking which request path
        will be handled by which controller method.
        http://localhost:8080/actuator/mappings
    
    **6 The /configprops Endpoint**   
        The /configprops endpoint offers all the configuration
        properties defined by @ConfigurationProperties bean,
        including your configuration properties defined in the
        application.properties or YAML files.
        http://localhost:8080/actuator/configprops

    **7 The /metrics Endpoint**
        The /metrics endpoint shows various metrics about
        the current application such as how much memory it
        is using, how much memory is free, the size of the
        heap used, the number of threads used, and so on.
        http://localhost:8080/actuator/metrics

    **8 The /env Endpoint**
        The /env endpoint exposes all the properties from the
        Spring’s ConfigurableEnvironment interface, such as
        a list of active profiles, application properties, system
        environment variables and so on.
        http://localhost:8080/actuator/env

    **8 The /threaddump Endpoint**
        Using /threaddumb endpoint, you can view your
        application’s thread dumb with running threads
        details and JVM stack trace.
        http://localhost:8080/actuator/threaddump

    **9 The /loggers Endpoint**
        The /loggers endpoint allows you to view and configure the log
        levels of your application at runtime.
        http://localhost:8080/actuator/loggers
        You can view the logging level of the specific logger:
        http://localhost:8080/actuator/loggers/{name}
        Ex:
        http://localhost:8080/actuator/loggers/com.pankajdets.springboot

        You can update the logging level of the logger at a runtime by sending a POST request
        to URL: http://localhost:8080/actuator/loggers/{name}
        Ex: http://localhost:8080/actuator/loggers/com.pankajdets.springboot

    **10 The /shutdown Endpoint**
        The /shutdown endpoint can be used to gracefully shut down the
        application.
        
        1. This endpoint not enabled by default. You can enable this
        endpoint by adding this property in application.properties file:
        Management.endpoint.shutdown.enabled=true
        2. After adding this property, we need to send the HTTP POST
        request to below endpoint:
        http://localhost:8080/actuator/shutdown
        3. Watch the console log for spring boot application shutdown


    **SpringBoot REST API Documentation using SpringDoc Open API**
    **Swagger API Documentation**
    
    ![100](https://user-images.githubusercontent.com/42623098/233857548-90beaedb-5458-48a2-b91b-4ff48183f769.jpg)

    Developement Steps
    
    ![101](https://user-images.githubusercontent.com/42623098/233857617-6a898466-dd02-4777-a562-15a8273b8d60.jpg)


    **Step 1: Add below dependency**
        <!-- https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.1.0</version>
        </dependency>

     restart application and hit url http://localhost:8080/swagger-ui/index.html
 

    **Step 2: Define General API information using Annotations**

    Annotate entry point class with below

    
            @OpenAPIDefinition(
            info =@Info(
                title = "Spring Boot REST API Documentation",
                description = "Spring Boot REST API Documentation",
                version ="v1.0.0",
                contact = @Contact(
                    name = "Pankaj Ray",
                    email ="pankajdets@gmail.com",
                    url ="https://www.linkedin.com/in/pankaj-kumar-ray/"
                ),
                license = @License(
                    name = "Apache 2.0",
                    url = "https://www.linkedin.com/in/pankaj-kumar-ray/license"
                )
            ),
            externalDocs =@ExternalDocumentation(
                description = "Spring Boot user Management Documentation",
                url = "https://www.linkedin.com/in/pankaj-kumar-ray/user-management.html"

            )
        )

     
<img width="960" alt="1" src="https://user-images.githubusercontent.com/42623098/233857694-afc39165-92e7-4d42-8b25-75c292e50fa3.PNG">



        **Step 3: Customizing Swagger API Documentation using Annotations**

        To Provide information about REST APIs
        
            @Tag(
                name = "CRUD REST API for USER Resource",
                description = "Create User, Update user, Get User, Get All users, Delete User"
            )
            @RestController // To this class spring MVC rest controller
            @AllArgsConstructor //to create constructor  for single argument  userService instance variable
            @RequestMapping("api/users") // satting base url at class level
            public class UserController {
                private UserService userService;// Constructor based dependency injection to inject userService in UserController class
                //typically we need to @Autowired annotation to inject the dependency
                // But spring 4.3 onwards whenever there is a spring bean with single parameterize constructor, We can omit @Autowired annotation
                // Here using @AllArgsConstructor we have created Constructor for single argument userService 
                

                //build Create User REST API
                @Operation(
                    summary = "Create User REST API",
                    description = "Create User REST API is used to save user in a database"
                )
                @ApiResponse(
                    responseCode = "201",
                    description = "Http Status 201 CREATED"
                )
                @PostMapping
                public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
                    UserDto savedUserDto = userService.createUser(userDto);
                    return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
                }

                //build get user by id REST API
                //http://localhost:8080/api/users/1
                @Operation(
                    summary = "GET User By ID REST API",
                    description = "Get User By ID  REST API is used to get single user from database"
                )
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status 200 SUCCESS"
                )
                @GetMapping("{id}")
                public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
                    UserDto userDto = userService.getUserById(userId);
                    return new ResponseEntity<>(userDto, HttpStatus.OK);
                }


                //build get all users REST API
                //http://localhost:8080/api/users
                @Operation(
                    summary = "GET All Users REST API",
                    description = "GET All Users  REST API is used to get all the  users from database"
                )
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status 200 SUCCESS"
                )
                @GetMapping
                public ResponseEntity<List<UserDto>> getAllusers(){
                    List<UserDto> usersDto = userService.getAllUsers();
                    return new ResponseEntity<>(usersDto, HttpStatus.OK);
                }

                //build update user REST API
                //http://localhost:8080/api/users/1
                @Operation(
                    summary = "Update User REST API",
                    description = "Update User  REST API is used to update a particular user in database"
                )
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status 200 SUCCESS"
                )
                @PutMapping("{id}")
                public ResponseEntity<UserDto> updateUSer(@PathVariable("id") Long userId, @RequestBody @Valid UserDto userDto){
                    userDto.setId(userId);
                    UserDto updatedUserDto = userService.updateUser(userDto);
                    return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
                }

                //build delete user REST API
                //http://localhost:8080/api/users/1
                @Operation(
                    summary = "Delete User REST API",
                    description = "Delete User  REST API is used to delete a particular user from the database"
                )
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status 200 SUCCESS"
                )
                @DeleteMapping("{id}")
                public ResponseEntity<String> deleteuser(@PathVariable("id") Long userId){
                    userService.deleteUser(userId);
                    return new ResponseEntity<String>("user Successfully deleted", HttpStatus.OK);
                }


            }
    
    
    <img width="843" alt="2" src="https://user-images.githubusercontent.com/42623098/233857736-c1c479bd-40be-4ee1-945c-c7cb46abb10d.PNG">




    **Steps 4: Customizing Swagger Models Documentation with Annotations**

        goto userDto class and use @Schema

            @Schema(
                description = "UserDto Model Information"
            )
            @Setter
            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            public class UserDto {
                private long id;
                
                @Schema(
                    description = "user First Name"    )
                //Requirement like User first name should not be null or empty
                @NotEmpty(message = "User first name should not be null or empty") 
                private String firstName;

                @Schema(
                    description = "User Last Name"
                )
                //Requirement like User last name should not be null or empty
                @NotEmpty(message = " User last name should not be null or empty")
                private String lastName;

                @Schema(
                    description = "User Email Address"
                )
                //Requirement like User email name should not be null or empty
                //Requirement like email address should be valid
                @NotEmpty(message = " User email name should not be null or empty")
                @Email(message = " email address should be valid")
                private String email;
            }





    <img width="899" alt="3" src="https://user-images.githubusercontent.com/42623098/233857782-a943fcdd-acbf-4af5-988b-a5a4c976e29b.PNG">





