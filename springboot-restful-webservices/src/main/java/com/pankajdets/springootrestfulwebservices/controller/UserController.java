package com.pankajdets.springootrestfulwebservices.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pankajdets.springootrestfulwebservices.model.User;
import com.pankajdets.springootrestfulwebservices.service.UserService;

import lombok.AllArgsConstructor;

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

    //build get user by id REST API
    //http://localhost:8080/api/users/1
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId){
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    //build get all users REST API
    //http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllusers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //build update user REST API
     //http://localhost:8080/api/users/1
    @PutMapping("{id}")
    public ResponseEntity<User> updateUSer(@PathVariable("id") Long userId, @RequestBody User user){
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }
}
