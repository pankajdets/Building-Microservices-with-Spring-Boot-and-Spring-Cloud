package com.pankajdets.springootrestfulwebservices.service.impl;

import java.util.List;
import java.util.Optional;

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

    @Override
    public User getUserById(Long userId) {
        Optional<User> optionalUser =userRepository.findById(userId);
        return optionalUser.get();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User updateUser(User user) {
       User existingUser = userRepository.findById(user.getId()).get();
       existingUser.setFirstName(user.getFirstName());
       existingUser.setLastName(user.getLastName());
       existingUser.setEmail(user.getEmail());
       User updatedUser = userRepository.save(existingUser);
       return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
       userRepository.deleteById(userId);
    }

    
    
}
