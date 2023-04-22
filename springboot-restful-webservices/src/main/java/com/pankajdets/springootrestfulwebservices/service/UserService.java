package com.pankajdets.springootrestfulwebservices.service;

import com.pankajdets.springootrestfulwebservices.model.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
}
