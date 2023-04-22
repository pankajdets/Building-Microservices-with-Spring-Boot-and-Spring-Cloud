package com.pankajdets.springootrestfulwebservices.service.impl;

import org.springframework.stereotype.Service;

import com.pankajdets.springootrestfulwebservices.model.User;
import com.pankajdets.springootrestfulwebservices.repository.UserRepository;
import com.pankajdets.springootrestfulwebservices.service.UserService;

import lombok.AllArgsConstructor;

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
