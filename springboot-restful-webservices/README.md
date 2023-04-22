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

    


