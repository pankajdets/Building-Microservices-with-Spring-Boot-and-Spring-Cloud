package com.pankajdets.springbootrestfulwebservicesusingdto.service;

import java.util.List;

import com.pankajdets.springbootrestfulwebservicesusingdto.dto.UserDto;
import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;


public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<UserDto> getAllUsers();
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long userId);
}
