package com.pankajdets.springbootrestapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller // To make class MVC Controller
//@ResponseBody // It tells a controller that object returned is automatically serialized into JSON and passed back into HttpResponse object
//This annotation will convert java object to json
//@RestController = @Controller + @ResponseBody 
@RestController
public class HelloWorldController {
    

    //HTTP GET Request
    //http://localhost:8080/hello-world
    @GetMapping("/hello-world")
    public String helloWorld(){
        return "Hello World!";
    }
}
