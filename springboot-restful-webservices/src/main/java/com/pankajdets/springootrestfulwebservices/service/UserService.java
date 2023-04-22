package com.pankajdets.springootrestfulwebservices.service;

import java.util.List;

import com.pankajdets.springootrestfulwebservices.model.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
    List<User> getAllUsers();
}
