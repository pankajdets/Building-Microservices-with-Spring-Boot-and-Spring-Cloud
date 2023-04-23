package com.pankajdets.springbootrestfulwebservicesusingdto.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.pankajdets.springbootrestfulwebservicesusingdto.dto.UserDto;
import com.pankajdets.springbootrestfulwebservicesusingdto.exception.ErrorDetails;
import com.pankajdets.springbootrestfulwebservicesusingdto.exception.ResourceNotFoundException;
import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;
import com.pankajdets.springbootrestfulwebservicesusingdto.service.UserService;

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
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto savedUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    //build get user by id REST API
    //http://localhost:8080/api/users/1
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    //build get all users REST API
    //http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllusers(){
        List<UserDto> usersDto = userService.getAllUsers();
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    //build update user REST API
     //http://localhost:8080/api/users/1
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUSer(@PathVariable("id") Long userId, @RequestBody UserDto userDto){
        userDto.setId(userId);
        UserDto updatedUserDto = userService.updateUser(userDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    //build delete user REST API
    //http://localhost:8080/api/users/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteuser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<String>("user Successfully deleted", HttpStatus.OK);
    }

//     @ExceptionHandler(ResourceNotFoundException.class) // To handle Specific Exception and return custom Error Response back to client
//     public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
//                                                                        WebRequest webRequest){

//        ErrorDetails errorDetails = new ErrorDetails(
//                LocalDateTime.now(),
//                exception.getMessage(),
//                webRequest.getDescription(false),
//                "USER_NOT_FOUND"
//        );

//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//    }
}

