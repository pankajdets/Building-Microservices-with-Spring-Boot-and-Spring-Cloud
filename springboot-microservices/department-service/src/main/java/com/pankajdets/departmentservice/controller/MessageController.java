package com.pankajdets.departmentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope // This anotation will force this spring bean to reload the configuration
@RestController
public class MessageController { 

    @Value("${spring.boot.message}")// To read value from configuration file
    private String message;

    @GetMapping("message")
    public String message(){
        return message;
    }
    
}
