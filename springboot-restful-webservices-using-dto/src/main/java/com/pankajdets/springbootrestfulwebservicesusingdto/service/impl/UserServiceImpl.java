package com.pankajdets.springbootrestfulwebservicesusingdto.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.pankajdets.springbootrestfulwebservicesusingdto.dto.UserDto;
import com.pankajdets.springbootrestfulwebservicesusingdto.mapper.AutoUserMapper;
import com.pankajdets.springbootrestfulwebservicesusingdto.mapper.UserMapper;
import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;
import com.pankajdets.springbootrestfulwebservicesusingdto.repository.UserRepository;
import com.pankajdets.springbootrestfulwebservicesusingdto.service.UserService;

import lombok.AllArgsConstructor;

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
