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


**Spring Boot REST API Exception Handling Overview**

**Create And Use ResourceNotFound Custom Exception**
    Requirement


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

We were using Spring Boot default execution handler


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

        